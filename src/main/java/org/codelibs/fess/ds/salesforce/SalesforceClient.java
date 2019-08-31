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

import java.io.IOException;
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
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.codelibs.fess.ds.salesforce.utils.AuthUtils;
import org.codelibs.fess.ds.salesforce.utils.BulkUtils;
import org.codelibs.fess.ds.salesforce.api.SearchData;
import org.codelibs.fess.ds.salesforce.api.SearchLayout;
import org.codelibs.fess.ds.salesforce.api.sobject.StandardObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesforceClient {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceClient.class);

    protected static String BASE_URL = "https://login.salesforce.com";
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
    protected static final String REFRESH_TOKEN_INTERVAL = "refresh_token_interval";

    protected static final String TITLE_PARAM = "title";
    protected static final String CONTENTS_PARAM = "contents";
    protected static final String DIGESTS_PARAM = "digests";
    protected static final String THUMBNAIL_PARAM = "thumbnail";
    protected static final String CUSTOM_PARAM = "custom";

    protected static final String OAUTH = "oauth";
    protected static final String PASSWORD = "password";

    protected final Map<String, String> paramMap;
    protected TimeoutTask refreshTokenTask;
    final ConnectionProvider connectionProvider;
    protected final String instanceUrl;
    protected final ObjectMapper mapper;

    public SalesforceClient(final Map<String, String> paramMap) {
        this.paramMap = paramMap;
        connectionProvider = new ConnectionProvider(paramMap);
        instanceUrl = connectionProvider.getPartnerConnection().getConfig().getServiceEndpoint().replaceFirst("/services/.*", "");
        refreshTokenTask = TimeoutManager.getInstance().addTimeoutTarget(connectionProvider,
                Integer.parseInt(paramMap.getOrDefault(REFRESH_TOKEN_INTERVAL, DEFAULT_REFRESH_TOKEN_INTERVAL)), true);
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }


    public void getStandardObjects(final Consumer<SearchData> consumer) {
        Arrays.stream(StandardObject.values()).forEach(o -> {
            final BulkConnection bulk = connectionProvider.getBulkConnection();
            final JobInfo job = BulkUtils.createJob(bulk, o.name());
            final SearchLayout layout = getSearchLayout(o);
            final String query = BulkUtils.createQuery(o.name(), layout.fields());
            final BatchInfo batch = BulkUtils.createBatch(bulk, job, query);
            BulkUtils.getQueryResultStream(bulk, job, batch).forEach( stream -> {
                try {
                    mapper.readTree(stream).forEach(a -> {
                        final SearchData data = new SearchData(o.name(), a, layout);
                        consumer.accept(data);
                    });
                } catch (final IOException e) {
                    logger.warn("Failed to deserialize the JSON content.", e);
                }
            });
    });
    }

    public void getCustomObjects(final Consumer<SearchData> consumer) {
        if(paramMap.get(CUSTOM_PARAM) == null) return ;
        for (String c : Arrays.stream(paramMap.get(CUSTOM_PARAM).split(",")).map(String::trim).collect(Collectors.toList())) {
            final BulkConnection bulk = connectionProvider.getBulkConnection();
            final JobInfo job = BulkUtils.createJob(bulk, c);
            final SearchLayout layout = getSearchLayout(c);
            final String query = BulkUtils.createQuery(c, layout.fields());
            final BatchInfo batch = BulkUtils.createBatch(bulk, job, query);
            BulkUtils.getQueryResultStream(bulk, job, batch).forEach(stream -> {
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

    private SearchLayout getSearchLayout(final StandardObject obj) {
        final String title =
                paramMap.get(obj.name() + "." + TITLE_PARAM) != null ?
                        paramMap.get(obj.name() + "." + TITLE_PARAM)
                        : obj.getLayout().getTitle();
        final List<String> contents =
                paramMap.get(obj.name() + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(obj.name() + "." + CONTENTS_PARAM)
                                .split(","))
                                .map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getContents();
        final List<String> digests =
                paramMap.get(obj.name() + "." + DIGESTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(obj.name() + "." + DIGESTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getDigests();
        final String thumbnail =
                paramMap.get(obj.name() + "." + THUMBNAIL_PARAM) != null ?
                        paramMap.get(obj.name() + "." + THUMBNAIL_PARAM)
                        : obj.getLayout().getThumbnail();
        return new SearchLayout(title, contents, digests, thumbnail);
    }

    private SearchLayout getSearchLayout(final String type) {
        final String title =
                paramMap.get(type + "." + TITLE_PARAM) != null ?
                        paramMap.get(type + "." + TITLE_PARAM)
                        : type;
        final List<String> contents =
                paramMap.get(type + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(type + "." + CONTENTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : emptyList();
        final List<String> digests = paramMap.get(type + "." + DIGESTS_PARAM) != null ?
                Arrays.stream(paramMap.get(type + "." + DIGESTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                : emptyList();
        final String thumbnail = paramMap.get(type + "." + THUMBNAIL_PARAM);
        return new SearchLayout(title, contents, digests, thumbnail);
    }

    // TODO : refactoring
    protected static class ConnectionProvider implements TimeoutTarget {

        final String authType;
        final String username;
        final String password;
        final String privateKey;
        final String securityToken;
        final String clientId;
        final String clientSecret;
        final String baseUrl;
        final Long refreshInterval;
        protected PartnerConnection partnerConnection;
        protected BulkConnection bulkConnection;

        public ConnectionProvider(final Map<String, String> paramMap) {
            username = paramMap.get(USERNAME_PARAM);
            password = paramMap.get(PASSWORD_PARAM);
            privateKey = paramMap.get(PRIVATE_KEY_PARAM);
            securityToken = paramMap.get(SECURITY_TOKEN_PARAM);
            clientId = paramMap.get(CLIENT_ID_PARAM);
            clientSecret = paramMap.get(CLIENT_SECRET_PARAM);
            baseUrl = paramMap.get(BASE_URL_PARAM) != null ? paramMap.get(BASE_URL_PARAM) : BASE_URL;
            authType = paramMap.get(AUTH_TYPE_PARAM);
            refreshInterval = Long.parseLong(paramMap.getOrDefault(REFRESH_TOKEN_INTERVAL, DEFAULT_REFRESH_TOKEN_INTERVAL));
            partnerConnection = getConnection();
            try {
                bulkConnection = BulkUtils.getBulkConnection(partnerConnection);
            } catch (final AsyncApiException e) {
                throw new SalesforceDataStoreException("Failed to create connection.", e);
            }
        }

        public BulkConnection getBulkConnection() {
            return bulkConnection;
        }

        public PartnerConnection getPartnerConnection() {
            return partnerConnection;
        }

        private PartnerConnection getConnection() {
            switch(authType) {
                case OAUTH: {
                    if (username == null || clientId == null || privateKey == null) {
                        throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + CLIENT_ID_PARAM + "', '" + PRIVATE_KEY_PARAM + "'required for OAuth.");
                    }
                    try {
                        return AuthUtils.getConnection(username, clientId, privateKey, baseUrl, refreshInterval);
                    } catch (final ConnectionException e) {
                        throw new SalesforceDataStoreException("Failed to get connection by OAuth", e);
                    }
                }
                case PASSWORD: {
                    if (username == null || password == null || securityToken == null || clientId == null || clientSecret == null) {
                        throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + PASSWORD_PARAM + "', '" + SECURITY_TOKEN_PARAM +
                                "', '" + CLIENT_ID_PARAM + "', '" + CLIENT_SECRET_PARAM + "' required for Basic Auth.");
                    }
                    try {
                        return AuthUtils.getConnectionByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
                    } catch (final ConnectionException e) {
                        throw new SalesforceDataStoreException("Failed to get connection by Basic Auth.", e);
                    }
                }
                default: {
                    throw new SalesforceDataStoreException("parameter '" + AUTH_TYPE_PARAM + "' required.");
                }
            }
        }

        @Override
        public void expired() {
            if (authType.equals(OAUTH)) {
                refreshToken();
            }
        }

        private void refreshToken() {
            final ConnectorConfig currentConfig = partnerConnection.getConfig();
            final TokenResponse response = AuthUtils.refreshToken(clientSecret, currentConfig.getSessionId(), baseUrl);
            if (response.getAccessToken() == null) {
                throw new SalesforceDataStoreException("Failed to refresh token." + response.getError() + " : " + response.getErrorDescription());
            }
            final ConnectorConfig newConfig = AuthUtils.createConnectorConfig(response);
            try {
                partnerConnection = Connector.newConnection(newConfig);
                bulkConnection = BulkUtils.getBulkConnection(partnerConnection);
            } catch (final Exception e) {
                throw new SalesforceDataStoreException("Failed to get new connection.", e);
            }
        }
    }
}
