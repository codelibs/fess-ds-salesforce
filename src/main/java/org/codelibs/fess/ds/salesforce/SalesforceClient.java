/*
 * Copyright 2012-2025 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.fess.ds.salesforce;

import static java.util.Collections.emptyList;
import static org.codelibs.core.stream.StreamUtil.split;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codelibs.core.lang.StringUtil;
import org.codelibs.core.timer.TimeoutManager;
import org.codelibs.core.timer.TimeoutTarget;
import org.codelibs.core.timer.TimeoutTask;
import org.codelibs.curl.Curl;
import org.codelibs.curl.CurlException;
import org.codelibs.curl.CurlRequest;
import org.codelibs.curl.CurlResponse;
import org.codelibs.fess.ds.salesforce.api.SearchData;
import org.codelibs.fess.ds.salesforce.api.SearchLayout;
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.codelibs.fess.ds.salesforce.api.sobject.StandardObject;
import org.codelibs.fess.ds.salesforce.util.AuthUtil;
import org.codelibs.fess.ds.salesforce.util.BulkUtil;
import org.codelibs.fess.entity.DataStoreParams;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BulkConnection;
import com.sforce.async.JobInfo;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * A client for accessing Salesforce data, supporting both standard and custom objects.
 * It handles authentication, API communication, and data retrieval.
 */
public class SalesforceClient implements Closeable {

    /** Logger instance. */
    protected static final Logger logger = LogManager.getLogger(SalesforceClient.class);

    /** Jackson object mapper for JSON processing. */
    protected static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /** Default Salesforce login URL. */
    protected static final String BASE_URL = "https://login.salesforce.com";
    /** Salesforce API version. */
    protected static final String API_VERSION = "46.0";

    /** Default interval in seconds for refreshing the authentication token. */
    protected static String DEFAULT_REFRESH_TOKEN_INTERVAL = "3540";

    // parameters
    /** Parameter key for the Salesforce base URL. */
    protected static final String BASE_URL_PARAM = "base_url";
    /** Parameter key for the authentication type. */
    protected static final String AUTH_TYPE_PARAM = "auth_type";
    /** Parameter key for the username. */
    protected static final String USERNAME_PARAM = "username";
    /** Parameter key for the password. */
    protected static final String PASS_PARAM = "password";
    /** Parameter key for the security token. */
    protected static final String SECURITY_TOKEN_PARAM = "security_token";
    /** Parameter key for the client ID. */
    protected static final String CLIENT_ID_PARAM = "client_id";
    /** Parameter key for the client secret. */
    protected static final String CLIENT_SECRET_PARAM = "client_secret";
    /** Parameter key for the private key. */
    protected static final String PRIVATE_KEY_PARAM = "private_key";
    /** Parameter key for the title field mapping. */
    protected static final String TITLE_PARAM = "title";
    /** Parameter key for the contents field mapping. */
    protected static final String CONTENTS_PARAM = "contents";
    /** Parameter key for the descriptions field mapping. */
    protected static final String DESCRIPTIONS_PARAM = "descriptions";
    /** Parameter key for the thumbnail field mapping. */
    protected static final String THUMBNAIL_PARAM = "thumbnail";
    /** Parameter key for the custom objects list. */
    protected static final String CUSTOM_PARAM = "custom";
    /** Parameter key for the token refresh interval. */
    protected static final String REFRESH_TOKEN_INTERVAL_PARAM = "refresh_token_interval";
    /** Parameter key for the proxy host. */
    protected static final String PROXY_HOST_PARAM = "proxy_host";
    /** Parameter key for the proxy port. */
    protected static final String PROXY_PORT_PARAM = "proxy_port";

    // values for parameters
    /** Value for OAuth token-based authentication. */
    protected static final String OAUTH_TOKEN = "oauth_token";
    /** Value for OAuth password-based authentication. */
    protected static final String OAUTH_PASS = "oauth_password";

    /** The data store parameters. */
    protected final DataStoreParams paramMap;
    /** The scheduled task for refreshing the token. */
    protected TimeoutTask refreshTokenTask;
    /** The provider for Salesforce connections. */
    protected final ConnectionProvider connectionProvider;
    /** The Salesforce instance URL. */
    protected final String instanceUrl;

    /**
     * Constructs a new SalesforceClient with the given parameters.
     *
     * @param paramMap The data store parameters.
     */
    public SalesforceClient(final DataStoreParams paramMap) {
        this.paramMap = paramMap;
        connectionProvider = new ConnectionProvider(paramMap);
        instanceUrl = connectionProvider.getPartnerConnection().getConfig().getServiceEndpoint().replaceFirst("/services/.*", "");
        refreshTokenTask = TimeoutManager.getInstance().addTimeoutTarget(connectionProvider,
                Integer.parseInt(paramMap.getAsString(REFRESH_TOKEN_INTERVAL_PARAM, DEFAULT_REFRESH_TOKEN_INTERVAL)), true);
    }

    @Override
    public void close() {
        if (refreshTokenTask != null) {
            refreshTokenTask.cancel();
        }
    }

    /**
     * Returns the Salesforce instance URL.
     *
     * @return The instance URL.
     */
    public String getInstanceUrl() {
        return instanceUrl;
    }

    /**
     * Retrieves standard Salesforce objects and processes them.
     *
     * @param consumer The consumer to accept the retrieved {@link SearchData}.
     * @param ignoreError If true, errors during processing will be ignored.
     */
    public void getStandardObjects(final Consumer<SearchData> consumer, final boolean ignoreError) {
        for (final StandardObject so : StandardObject.values()) {
            final String soName = convertSnakeToCamel(so.name());
            final BulkConnection bulk = connectionProvider.getBulkConnection();
            final JobInfo job = BulkUtil.createJob(bulk, soName);
            final SearchLayout layout = getSearchLayout(so);
            final String query = BulkUtil.createQuery(soName, layout.fields());
            final BatchInfo batch = BulkUtil.createBatch(bulk, job, query);

            BulkUtil.getQueryResultStream(bulk, job, batch, ignoreError).forEach(stream -> {
                try (stream) {
                    mapper.readTree(stream).forEach(a -> {
                        final SearchData data = new SearchData(soName, a, layout);
                        consumer.accept(data);
                    });
                } catch (final IOException e) {
                    logger.warn("Failed to deserialize the JSON content.", e);
                }
            });

            try {
                bulk.closeJob(job.getId());
            } catch (final AsyncApiException e) {
                logger.warn("Failed to close the job : {}. {}", job.getId(), e);
            }
        }
    }

    /**
     * Converts a string from snake_case to CamelCase.
     *
     * @param snakeString The string in snake_case.
     * @return The converted string in CamelCase.
     */
    protected static String convertSnakeToCamel(final String snakeString) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, snakeString);
    }

    /**
     * Retrieves custom Salesforce objects and processes them.
     *
     * @param consumer The consumer to accept the retrieved {@link SearchData}.
     * @param ignoreError If true, errors during processing will be ignored.
     */
    public void getCustomObjects(final Consumer<SearchData> consumer, final boolean ignoreError) {
        if (paramMap.get(CUSTOM_PARAM) == null) {
            return;
        }
        final String[] customObjects = split(paramMap.getAsString(CUSTOM_PARAM), ",")
                .get(stream -> stream.map(String::trim).filter(StringUtil::isNotBlank).toArray(n -> new String[n]));
        for (final String co : customObjects) {

            final BulkConnection bulk = connectionProvider.getBulkConnection();
            final JobInfo job = BulkUtil.createJob(bulk, co);
            final SearchLayout layout = getSearchLayout(co);
            final String query = BulkUtil.createQuery(co, layout.fields());
            final BatchInfo batch = BulkUtil.createBatch(bulk, job, query);

            BulkUtil.getQueryResultStream(bulk, job, batch, ignoreError).forEach(stream -> {
                try (stream) {
                    mapper.readTree(stream).forEach(a -> {
                        final SearchData data = new SearchData(co, a, layout);
                        consumer.accept(data);
                    });
                } catch (final IOException e) {
                    logger.warn("Failed to deserialize the JSON content.", e);
                }
            });

            try {
                bulk.closeJob(job.getId());
            } catch (final AsyncApiException e) {
                logger.warn("Failed to close the job : {}. {}", job.getId(), e);
            }
        }
    }

    /**
     * Gets the search layout for a standard object.
     *
     * @param obj The standard object.
     * @return The search layout.
     */
    protected SearchLayout getSearchLayout(final StandardObject obj) {
        final String name = obj.name();
        final String title = paramMap.getAsString(name + "." + TITLE_PARAM, obj.getLayout().getTitle());
        final List<String> contents = paramMap.get(name + "." + CONTENTS_PARAM) instanceof final String s
                ? split(s, ",").get(stream -> stream.map(String::trim).filter(StringUtil::isNotBlank).collect(Collectors.toList()))
                : obj.getLayout().getContents();
        final List<String> descriptions = paramMap.get(name + "." + DESCRIPTIONS_PARAM) instanceof final String s
                ? split(s, ",").get(stream -> stream.map(String::trim).filter(StringUtil::isNotBlank).collect(Collectors.toList()))
                : obj.getLayout().getDescriptions();
        final String thumbnail = paramMap.getAsString(name + "." + THUMBNAIL_PARAM, obj.getLayout().getThumbnail());
        return new SearchLayout(title, contents, descriptions, thumbnail);
    }

    /**
     * Gets the search layout for a custom object.
     *
     * @param type The custom object type.
     * @return The search layout.
     */
    protected SearchLayout getSearchLayout(final String type) {
        final String title = paramMap.getAsString(type + "." + TITLE_PARAM, type);
        final List<String> contents = paramMap.get(type + "." + CONTENTS_PARAM) instanceof final String s
                ? split(s, ",").get(stream -> stream.map(String::trim).filter(StringUtil::isNotBlank).collect(Collectors.toList()))
                : emptyList();
        final List<String> descriptions = paramMap.get(type + "." + DESCRIPTIONS_PARAM) instanceof final String s
                ? split(s, ",").get(stream -> stream.map(String::trim).filter(StringUtil::isNotBlank).collect(Collectors.toList()))
                : emptyList();
        final String thumbnail = paramMap.getAsString(type + "." + THUMBNAIL_PARAM);
        return new SearchLayout(title, contents, descriptions, thumbnail);
    }

    /**
     * Provides and manages connections to Salesforce.
     */
    protected static class ConnectionProvider implements TimeoutTarget {

        /** Logger instance. */
        protected static final Logger logger = LogManager.getLogger(ConnectionProvider.class);

        /** The authentication type. */
        protected final String authType;
        /** The Salesforce username. */
        protected final String username;
        /** The Salesforce password. */
        protected final String pass;
        /** The private key for JWT authentication. */
        protected final String privateKey;
        /** The security token for password authentication. */
        protected final String securityToken;
        /** The connected app's client ID. */
        protected final String clientId;
        /** The connected app's client secret. */
        protected final String clientSecret;
        /** The Salesforce base URL. */
        protected final String baseUrl;
        /** The token refresh interval. */
        protected final Long refreshInterval;
        /** The proxy for the connection. */
        protected Proxy proxy;
        /** The Partner API connection. */
        protected PartnerConnection partnerConnection;
        /** The Bulk API connection. */
        protected BulkConnection bulkConnection;

        /**
         * Constructs a new ConnectionProvider.
         *
         * @param paramMap The data store parameters.
         */
        protected ConnectionProvider(final DataStoreParams paramMap) {
            username = paramMap.getAsString(USERNAME_PARAM);
            pass = paramMap.getAsString(PASS_PARAM);
            privateKey = paramMap.getAsString(PRIVATE_KEY_PARAM);
            securityToken = paramMap.getAsString(SECURITY_TOKEN_PARAM);
            clientId = paramMap.getAsString(CLIENT_ID_PARAM);
            clientSecret = paramMap.getAsString(CLIENT_SECRET_PARAM);
            baseUrl = paramMap.getAsString(BASE_URL_PARAM, BASE_URL);
            authType = paramMap.getAsString(AUTH_TYPE_PARAM);

            final String httpProxyHost = paramMap.getAsString(PROXY_HOST_PARAM);
            final String httpProxyPort = paramMap.getAsString(PROXY_PORT_PARAM);
            if (httpProxyHost != null) {
                if (httpProxyPort == null) {
                    throw new SalesforceDataStoreException("parameter " + "'" + PROXY_PORT_PARAM + "' required.");
                }
                try {
                    final int port = Integer.parseInt(httpProxyPort);
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxyHost, port));
                } catch (final NumberFormatException e) {
                    throw new SalesforceDataStoreException("parameter " + "'" + PROXY_PORT_PARAM + "' invalid.", e);
                }
            }

            refreshInterval = Long.parseLong(paramMap.getAsString(REFRESH_TOKEN_INTERVAL_PARAM, DEFAULT_REFRESH_TOKEN_INTERVAL));
            partnerConnection = getConnection();
            try {
                bulkConnection = getBulkConnection(partnerConnection);
            } catch (final AsyncApiException e) {
                throw new SalesforceDataStoreException("Failed to create bulk connection.", e);
            }
        }

        @Override
        public void expired() {
            if (OAUTH_TOKEN.equals(authType)) {
                refreshConnection();
            }
        }

        /**
         * Returns the Bulk API connection.
         *
         * @return The Bulk API connection.
         */
        protected BulkConnection getBulkConnection() {
            return bulkConnection;
        }

        /**
         * Returns the Partner API connection.
         *
         * @return The Partner API connection.
         */
        protected PartnerConnection getPartnerConnection() {
            return partnerConnection;
        }

        /**
         * Establishes a new Partner API connection based on the configured authentication type.
         *
         * @return The established PartnerConnection.
         */
        protected PartnerConnection getConnection() {
            switch (authType) {
            case OAUTH_TOKEN: {
                if (username == null || clientId == null || privateKey == null) {
                    throw new SalesforceDataStoreException("parameters '{" + USERNAME_PARAM + "}', '{" + CLIENT_ID_PARAM + "', '"
                            + PRIVATE_KEY_PARAM + "' required for token authentication.");
                }
                try {
                    return getConnectionByToken();
                } catch (final ConnectionException e) {
                    throw new SalesforceDataStoreException("Failed to get connection by token authentication", e);
                }
            }
            case OAUTH_PASS: {
                if (username == null || pass == null || securityToken == null || clientId == null || clientSecret == null) {
                    throw new SalesforceDataStoreException(
                            "parameters '" + USERNAME_PARAM + "', '" + PASS_PARAM + "', '" + SECURITY_TOKEN_PARAM + "', '" + CLIENT_ID_PARAM
                                    + "', '" + CLIENT_SECRET_PARAM + "' required for password authentication.");
                }
                try {
                    return getConnectionByPass();
                } catch (final ConnectionException e) {
                    throw new SalesforceDataStoreException("Failed to get connection by password authentication.", e);
                }
            }
            default: {
                throw new SalesforceDataStoreException("parameter '" + AUTH_TYPE_PARAM + "' invalid.");
            }
            }
        }

        /**
         * Refreshes the Salesforce connections (Partner and Bulk).
         */
        protected void refreshConnection() {
            if (logger.isDebugEnabled()) {
                logger.debug("Refreshing access token and connection.");
            }
            partnerConnection = getConnection();
            if (logger.isDebugEnabled()) {
                logger.debug("Access token and connection was successfully refreshed.");
            }
            try {
                bulkConnection = getBulkConnection(partnerConnection);
            } catch (final Exception e) {
                throw new SalesforceDataStoreException("Failed to create bulk connection.", e);
            }
        }

        /**
         * Establishes a Partner API connection using OAuth JWT Bearer Token Flow.
         *
         * @return The established PartnerConnection.
         * @throws ConnectionException If a connection error occurs.
         */
        protected PartnerConnection getConnectionByToken() throws ConnectionException {
            final TokenResponse response = getTokenResponseByToken();
            if (response.getAccessToken() == null) {
                throw new SalesforceDataStoreException(
                        "Failed to get access token : " + "[" + response.getError() + " : " + response.getErrorDescription() + "]");
            }
            final ConnectorConfig config = createConnectorConfig(response);
            return Connector.newConnection(config);
        }

        /**
         * Establishes a Partner API connection using OAuth Username-Password Flow.
         *
         * @return The established PartnerConnection.
         * @throws ConnectionException If a connection error occurs.
         */
        protected PartnerConnection getConnectionByPass() throws ConnectionException {
            final TokenResponse response = getTokenResponseByPass();
            final ConnectorConfig config = createConnectorConfig(response);
            return Connector.newConnection(config);
        }

        /**
         * Creates a Bulk API connection from a Partner API connection.
         *
         * @param connection The Partner API connection.
         * @return The created BulkConnection.
         * @throws AsyncApiException If an API error occurs.
         */
        protected BulkConnection getBulkConnection(final PartnerConnection connection) throws AsyncApiException {
            final ConnectorConfig config = new ConnectorConfig();
            config.setSessionId(connection.getConfig().getSessionId());
            config.setRestEndpoint(
                    connection.getConfig().getServiceEndpoint().replaceFirst("/services.*", "/services/async/" + API_VERSION));
            if (proxy != null) {
                config.setProxy(proxy);
            }
            if (logger.isDebugEnabled()) {
                logger.info("Session Id : {}", config.getSessionId());
                logger.info("Rest Endpoint : {}", config.getRestEndpoint());
            }

            return new BulkConnection(config);
        }

        /**
         * Creates a ConnectorConfig from a token response.
         *
         * @param response The token response.
         * @return The created ConnectorConfig.
         */
        protected ConnectorConfig createConnectorConfig(final TokenResponse response) {
            final ConnectorConfig config = new ConnectorConfig();
            config.setSessionId(response.getAccessToken());
            config.setAuthEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION);
            config.setServiceEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION + "/" + response.getId());
            if (proxy != null) {
                config.setProxy(proxy);
            }
            if (logger.isDebugEnabled()) {
                logger.info("Auth Endpoint : {}", config.getAuthEndpoint());
                logger.info("Service Endpoint : {}", config.getServiceEndpoint());
            }
            return config;
        }

        /**
         * Retrieves a token response using the OAuth JWT Bearer Token Flow.
         *
         * @return The token response.
         */
        protected TokenResponse getTokenResponseByToken() {
            try {
                final String jwt = AuthUtil.createJWT(username, clientId, privateKey, baseUrl, refreshInterval);
                final CurlRequest request = Curl.post(baseUrl + "/services/oauth2/token")
                        .param("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer").param("assertion", jwt);
                if (proxy != null) {
                    request.proxy(proxy);
                }
                final CurlResponse response = request.execute();
                return parseTokenResponse(response.getContentAsStream());
            } catch (final Exception e) {
                throw new SalesforceDataStoreException("Failed to get token response .", e);
            }
        }

        /**
         * Retrieves a token response using the OAuth Username-Password Flow.
         *
         * @return The token response.
         */
        protected TokenResponse getTokenResponseByPass() {
            try {
                final CurlRequest request =
                        Curl.post(baseUrl + "/services/oauth2/token").param("grant_type", "password").param("username", username)
                                .param("password", pass + securityToken).param("client_id", clientId).param("client_secret", clientSecret);
                if (proxy != null) {
                    request.proxy(proxy);
                }
                final CurlResponse response = request.execute();
                return parseTokenResponse(response.getContentAsStream());
            } catch (final CurlException | IOException e) {
                throw new SalesforceDataStoreException("Failed to get token response.", e);
            }
        }

    } // class ConnectionProvider

    /**
     * Parses a token response from an input stream.
     *
     * @param content The input stream containing the JSON response.
     * @return The parsed {@link TokenResponse}.
     */
    protected static TokenResponse parseTokenResponse(final InputStream content) {
        try {
            return mapper.readValue(content, TokenResponse.class);
        } catch (final IOException e) {
            throw new SalesforceDataStoreException("Failed to parse token response.", e);
        }
    }

}