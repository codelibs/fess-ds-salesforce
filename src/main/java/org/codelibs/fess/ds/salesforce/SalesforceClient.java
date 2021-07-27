/*
 * Copyright 2012-2021 CodeLibs Project and the Others.
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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class SalesforceClient implements Closeable {

    protected static final Logger logger = LoggerFactory.getLogger(SalesforceClient.class);

    protected static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected static final String BASE_URL = "https://login.salesforce.com";
    protected static final String API_VERSION = "46.0";

    protected static String DEFAULT_REFRESH_TOKEN_INTERVAL = "3540";

    // parameters
    protected static final String BASE_URL_PARAM = "base_url";
    protected static final String AUTH_TYPE_PARAM = "auth_type";
    protected static final String USERNAME_PARAM = "username";
    protected static final String PASS_PARAM = "password";
    protected static final String SECURITY_TOKEN_PARAM = "security_token";
    protected static final String CLIENT_ID_PARAM = "client_id";
    protected static final String CLIENT_SECRET_PARAM = "client_secret";
    protected static final String PRIVATE_KEY_PARAM = "private_key";
    protected static final String TITLE_PARAM = "title";
    protected static final String CONTENTS_PARAM = "contents";
    protected static final String DESCRIPTIONS_PARAM = "descriptions";
    protected static final String THUMBNAIL_PARAM = "thumbnail";
    protected static final String CUSTOM_PARAM = "custom";
    protected static final String REFRESH_TOKEN_INTERVAL_PARAM = "refresh_token_interval";
    protected static final String PROXY_HOST_PARAM = "proxy_host";
    protected static final String PROXY_PORT_PARAM = "proxy_port";

    // values for parameters
    protected static final String OAUTH_TOKEN = "oauth_token";
    protected static final String OAUTH_PASS = "oauth_password";

    protected final Map<String, String> paramMap;
    protected TimeoutTask refreshTokenTask;
    protected final ConnectionProvider connectionProvider;
    protected final String instanceUrl;

    public SalesforceClient(final Map<String, String> paramMap) {
        this.paramMap = paramMap;
        connectionProvider = new ConnectionProvider(paramMap);
        instanceUrl = connectionProvider.getPartnerConnection().getConfig().getServiceEndpoint().replaceFirst("/services/.*", "");
        refreshTokenTask = TimeoutManager.getInstance().addTimeoutTarget(connectionProvider,
                Integer.parseInt(paramMap.getOrDefault(REFRESH_TOKEN_INTERVAL_PARAM, DEFAULT_REFRESH_TOKEN_INTERVAL)), true);
    }

    @Override
    public void close() {
        if (refreshTokenTask != null) {
            refreshTokenTask.cancel();
        }
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public void getStandardObjects(final Consumer<SearchData> consumer, final boolean ignoreError) throws InterruptedException {
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

    protected static String convertSnakeToCamel(final String snakeString) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, snakeString);
    }

    public void getCustomObjects(final Consumer<SearchData> consumer, final boolean ignoreError) throws InterruptedException {
        if (paramMap.get(CUSTOM_PARAM) == null) {
            return;
        }
        final String[] customObjects =
                Arrays.stream(StringUtil.split(paramMap.get(CUSTOM_PARAM), ",")).map(String::trim).toArray(String[]::new);
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

    protected SearchLayout getSearchLayout(final StandardObject obj) {
        final String title = paramMap.getOrDefault(obj.name() + "." + TITLE_PARAM, obj.getLayout().getTitle());
        final List<String> contents = paramMap.get(obj.name() + "." + CONTENTS_PARAM) != null
                ? Arrays.stream(paramMap.get(obj.name() + "." + CONTENTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                : obj.getLayout().getContents();
        final List<String> descriptions = paramMap.get(obj.name() + "." + DESCRIPTIONS_PARAM) != null ? Arrays
                .stream(paramMap.get(obj.name() + "." + DESCRIPTIONS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                : obj.getLayout().getDescriptions();
        final String thumbnail = paramMap.getOrDefault(obj.name() + "." + THUMBNAIL_PARAM, obj.getLayout().getThumbnail());
        return new SearchLayout(title, contents, descriptions, thumbnail);
    }

    protected SearchLayout getSearchLayout(final String type) {
        final String title = paramMap.getOrDefault(type + "." + TITLE_PARAM, type);
        final List<String> contents = paramMap.get(type + "." + CONTENTS_PARAM) != null
                ? Arrays.stream(paramMap.get(type + "." + CONTENTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                : emptyList();
        final List<String> descriptions = paramMap.get(type + "." + DESCRIPTIONS_PARAM) != null
                ? Arrays.stream(paramMap.get(type + "." + DESCRIPTIONS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                : emptyList();
        final String thumbnail = paramMap.get(type + "." + THUMBNAIL_PARAM);
        return new SearchLayout(title, contents, descriptions, thumbnail);
    }

    protected static class ConnectionProvider implements TimeoutTarget {

        protected static final Logger logger = LoggerFactory.getLogger(ConnectionProvider.class);

        protected final String authType;
        protected final String username;
        protected final String pass;
        protected final String privateKey;
        protected final String securityToken;
        protected final String clientId;
        protected final String clientSecret;
        protected final String baseUrl;
        protected final Long refreshInterval;
        protected Proxy proxy;
        protected PartnerConnection partnerConnection;
        protected BulkConnection bulkConnection;

        protected ConnectionProvider(final Map<String, String> paramMap) {
            username = paramMap.get(USERNAME_PARAM);
            pass = paramMap.get(PASS_PARAM);
            privateKey = paramMap.get(PRIVATE_KEY_PARAM);
            securityToken = paramMap.get(SECURITY_TOKEN_PARAM);
            clientId = paramMap.get(CLIENT_ID_PARAM);
            clientSecret = paramMap.get(CLIENT_SECRET_PARAM);
            baseUrl = paramMap.get(BASE_URL_PARAM) != null ? paramMap.get(BASE_URL_PARAM) : BASE_URL;
            authType = paramMap.get(AUTH_TYPE_PARAM);

            final String httpProxyHost = paramMap.get(PROXY_HOST_PARAM);
            final String httpProxyPort = paramMap.get(PROXY_PORT_PARAM);
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

            refreshInterval = Long.parseLong(paramMap.getOrDefault(REFRESH_TOKEN_INTERVAL_PARAM, DEFAULT_REFRESH_TOKEN_INTERVAL));
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

        protected BulkConnection getBulkConnection() {
            return bulkConnection;
        }

        protected PartnerConnection getPartnerConnection() {
            return partnerConnection;
        }

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

        protected PartnerConnection getConnectionByToken() throws ConnectionException {
            final TokenResponse response = getTokenResponseByToken();
            if (response.getAccessToken() == null) {
                throw new SalesforceDataStoreException(
                        "Failed to get access token : " + "[" + response.getError() + " : " + response.getErrorDescription() + "]");
            }
            final ConnectorConfig config = createConnectorConfig(response);
            return Connector.newConnection(config);
        }

        protected PartnerConnection getConnectionByPass() throws ConnectionException {
            final TokenResponse response = getTokenResponseByPass();
            final ConnectorConfig config = createConnectorConfig(response);
            return Connector.newConnection(config);
        }

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

    protected static TokenResponse parseTokenResponse(final InputStream content) {
        try {
            return mapper.readValue(content, TokenResponse.class);
        } catch (final IOException e) {
            throw new SalesforceDataStoreException("Failed to parse token response.", e);
        }
    }

}
