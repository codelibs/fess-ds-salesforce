/*
 * Copyright 2012-2019 CodeLibs Project and the Others.
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BulkConnection;
import com.sforce.async.JobInfo;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.codelibs.core.timer.TimeoutManager;
import org.codelibs.core.timer.TimeoutTarget;
import org.codelibs.core.timer.TimeoutTask;
import org.codelibs.curl.Curl;
import org.codelibs.curl.CurlException;
import org.codelibs.curl.CurlResponse;
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.codelibs.fess.ds.salesforce.util.AuthUtil;
import org.codelibs.fess.ds.salesforce.util.BulkUtil;
import org.codelibs.fess.ds.salesforce.api.SearchData;
import org.codelibs.fess.ds.salesforce.api.SearchLayout;
import org.codelibs.fess.ds.salesforce.api.sobject.StandardObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesforceClient implements Closeable {

    protected static final Logger logger = LoggerFactory.getLogger(SalesforceClient.class);

    protected static final ObjectMapper mapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected static final String BASE_URL = "https://login.salesforce.com";
    protected static final String API_VERSION = "46.0";

    protected static String DEFAULT_REFRESH_TOKEN_INTERVAL = "3540";

    // parameters
    protected static final String BASE_URL_PARAM = "base_url";
    protected static final String AUTH_TYPE_PARAM = "auth_type";
    protected static final String USERNAME_PARAM = "username";
    protected static final String PASSWORD_PARAM = "password";
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

    protected static final String TOKEN = "token";
    protected static final String PASSWORD = "password";

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


    public void getStandardObjects(final Consumer<SearchData> consumer, final boolean ignoreError) {
        Arrays.stream(StandardObject.values()).forEach(so -> {
            final BulkConnection bulk = connectionProvider.getBulkConnection();
            final JobInfo job = BulkUtil.createJob(bulk, so.name());
            final SearchLayout layout = getSearchLayout(so);
            final String query = BulkUtil.createQuery(so.name(), layout.fields());
            final BatchInfo batch = BulkUtil.createBatch(bulk, job, query);
            BulkUtil.getQueryResultStream(bulk, job, batch, ignoreError).forEach(stream -> {
                try {
                    mapper.readTree(stream).forEach(a -> {
                        final SearchData data = new SearchData(so.name(), a, layout);
                        consumer.accept(data);
                    });
                } catch (final IOException e) {
                    logger.warn("Failed to deserialize the JSON content.", e);
                }
            });
    });
    }

    public void getCustomObjects(final Consumer<SearchData> consumer, final boolean ignoreError) {
        if(paramMap.get(CUSTOM_PARAM) == null) return ;
        final List<String> customObjects = Arrays.stream(paramMap.get(CUSTOM_PARAM).split(",")).map(String::trim).collect(Collectors.toList());
        for (String c : customObjects) {
            final BulkConnection bulk = connectionProvider.getBulkConnection();
            final JobInfo job = BulkUtil.createJob(bulk, c);
            final SearchLayout layout = getSearchLayout(c);
            final String query = BulkUtil.createQuery(c, layout.fields());
            final BatchInfo batch = BulkUtil.createBatch(bulk, job, query);
            BulkUtil.getQueryResultStream(bulk, job, batch, ignoreError).forEach(stream -> {
                try {
                    mapper.readTree(stream).forEach(a -> {
                        final SearchData data = new SearchData(c, a, layout);
                        consumer.accept(data);
                    });
                } catch (final IOException e) {
                    logger.warn("Failed to deserialize the JSON content.", e);
                }
            });
        }
    }

    protected SearchLayout getSearchLayout(final StandardObject obj) {
        final String title = paramMap.getOrDefault(obj.name() + "." + TITLE_PARAM, obj.getLayout().getTitle());
        final List<String> contents =
                paramMap.get(obj.name() + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(obj.name() + "." + CONTENTS_PARAM)
                                .split(","))
                                .map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getContents();
        final List<String> descriptions =
                paramMap.get(obj.name() + "." + DESCRIPTIONS_PARAM) != null ?
                        Arrays.stream(paramMap.get(obj.name() + "." + DESCRIPTIONS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getDescriptions();
        final String thumbnail = paramMap.getOrDefault(obj.name() + "." + THUMBNAIL_PARAM, obj.getLayout().getThumbnail());
        return new SearchLayout(title, contents, descriptions, thumbnail);
    }

    protected SearchLayout getSearchLayout(final String type) {
        final String title = paramMap.getOrDefault(type + "." + TITLE_PARAM, type);
        final List<String> contents =
                paramMap.get(type + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(type + "." + CONTENTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : emptyList();
        final List<String> descriptions = paramMap.get(type + "." + DESCRIPTIONS_PARAM) != null ?
                Arrays.stream(paramMap.get(type + "." + DESCRIPTIONS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                : emptyList();
        final String thumbnail = paramMap.get(type + "." + THUMBNAIL_PARAM);
        return new SearchLayout(title, contents, descriptions, thumbnail);
    }

    protected static class ConnectionProvider implements TimeoutTarget {

        protected static final Logger logger = LoggerFactory.getLogger(ConnectionProvider.class);

        protected final String authType;
        protected final String username;
        protected final String password;
        protected final String privateKey;
        protected final String securityToken;
        protected final String clientId;
        protected final String clientSecret;
        protected final String baseUrl;
        protected final Long refreshInterval;
        protected PartnerConnection partnerConnection;
        protected BulkConnection bulkConnection;

        protected ConnectionProvider(final Map<String, String> paramMap) {
            username = paramMap.get(USERNAME_PARAM);
            password = paramMap.get(PASSWORD_PARAM);
            privateKey = paramMap.get(PRIVATE_KEY_PARAM);
            securityToken = paramMap.get(SECURITY_TOKEN_PARAM);
            clientId = paramMap.get(CLIENT_ID_PARAM);
            clientSecret = paramMap.get(CLIENT_SECRET_PARAM);
            baseUrl = paramMap.get(BASE_URL_PARAM) != null ? paramMap.get(BASE_URL_PARAM) : BASE_URL;
            authType = paramMap.get(AUTH_TYPE_PARAM);
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
            if (authType.equals(TOKEN)) {
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
            switch(authType) {
                case TOKEN: {
                    if (username == null || clientId == null || privateKey == null) {
                        throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + CLIENT_ID_PARAM + "', '" + PRIVATE_KEY_PARAM + "'required for Token Auth.");
                    }
                    try {
                        return getConnectionByToken(username, clientId, privateKey, baseUrl, refreshInterval);
                    } catch (final ConnectionException e) {
                        throw new SalesforceDataStoreException("Failed to get connection by Token Auth", e);
                    }
                }
                case PASSWORD: {
                    if (username == null || password == null || securityToken == null || clientId == null || clientSecret == null) {
                        throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + PASSWORD_PARAM + "', '" + SECURITY_TOKEN_PARAM +
                                "', '" + CLIENT_ID_PARAM + "', '" + CLIENT_SECRET_PARAM + "' required for Password Auth.");
                    }
                    try {
                        return getConnectionByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
                    } catch (final ConnectionException e) {
                        throw new SalesforceDataStoreException("Failed to get connection by Password Auth.", e);
                    }
                }
                default: {
                    throw new SalesforceDataStoreException("parameter '" + AUTH_TYPE_PARAM + "' required.");
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

        protected PartnerConnection getConnectionByToken(final String username, final String clientId, final String privateKeyPem,
                                                             final String baseUrl, final long refreshInterval) throws ConnectionException {
            final TokenResponse response = getTokenResponseByToken(username, clientId, privateKeyPem, baseUrl, refreshInterval);
            if (response.getAccessToken() == null) {
                throw new SalesforceDataStoreException("Failed to get access token : " + "[" + response.getError() + " : " + response.getErrorDescription() + "]");
            }
            final ConnectorConfig config = createConnectorConfig(response);
            return Connector.newConnection(config);
        }

        protected PartnerConnection getConnectionByPassword(final String username, final String password,
                                                                final String securityToken, final String clientId,
                                                                final String clientSecret, final String baseUrl) throws ConnectionException {
            final TokenResponse response = getTokenResponseByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
            final ConnectorConfig config = createConnectorConfig(response);
            return Connector.newConnection(config);
        }

        protected BulkConnection getBulkConnection(final PartnerConnection connection) throws AsyncApiException {
            final ConnectorConfig config = new ConnectorConfig();
            config.setSessionId(connection.getConfig().getSessionId());
            config.setRestEndpoint(connection.getConfig().getServiceEndpoint()
                    .replaceFirst("/services.*", "/services/async/" + API_VERSION));

            if (logger.isDebugEnabled()) {
                logger.info("Session Id : " + config.getSessionId());
                logger.info("Rest Endpoint : " + config.getRestEndpoint());
            }

            return new BulkConnection(config);
        }

        protected ConnectorConfig createConnectorConfig(final TokenResponse response) {
            final ConnectorConfig config = new ConnectorConfig();
            config.setSessionId(response.getAccessToken());
            config.setAuthEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION);
            config.setServiceEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION + "/" + response.getId());
            if (logger.isDebugEnabled()) {
                logger.info("Auth Endpoint : " + config.getAuthEndpoint());
                logger.info("Service Endpoint : " + config.getServiceEndpoint());
            }
            return config;
        }

        protected TokenResponse getTokenResponseByToken(final String username, final String clientId, final String privateKeyPem,
                                                        final String baseUrl, final long refreshInterval) {
            try {
                final String jwt = AuthUtil.createJWT(username, clientId, privateKeyPem, baseUrl, refreshInterval);
                final CurlResponse response = Curl.post(baseUrl + "/services/oauth2/token")
                        .param("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                        .param("assertion", jwt)
                        .execute();
                return parseTokenResponse(response.getContentAsStream());
            } catch (final Exception e) {
                throw new SalesforceDataStoreException("Failed to get token response .", e);
            }
        }

        protected TokenResponse getTokenResponseByPassword(final String username, final String password, final String securityToken,
                                                               final String clientId, final String clientSecret, final String baseUrl) {
            try {
                final CurlResponse response = Curl.post(baseUrl + "/services/oauth2/token")
                        .param("grant_type", "password")
                        .param("username", username)
                        .param("password", password + securityToken)
                        .param("client_id", clientId)
                        .param("client_secret", clientSecret)
                        .execute();
                return parseTokenResponse(response.getContentAsStream());
            }catch (final CurlException | IOException e) {
                throw new SalesforceDataStoreException("Failed to get token response.", e);
            }
        }

    }

    protected static TokenResponse parseTokenResponse(final InputStream content) {
        try {
            return mapper.readValue(content, TokenResponse.class);
        } catch (final IOException e) {
            throw new SalesforceDataStoreException("Failed to parse token response.", e);
        }
    }

}
