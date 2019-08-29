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
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BulkConnection;
import com.sforce.async.JobInfo;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import org.codelibs.core.lang.StringUtil;
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
    protected static final String DIGESTS_PARAM = "digests";
    protected static final String THUMBNAIL_PARAM = "thumbnail";
    protected static final String CUSTOM_PARAM = "custom";

    protected static final String OAUTH = "oauth";
    protected static final String PASSOWRD = "password";

    protected final Map<String, String> paramMap;
    protected final BulkConnection bulk;
    protected final String instanceUrl;
    protected final ObjectMapper mapper;

    public SalesforceClient(final Map<String, String> paramMap) {
        try {
            this.paramMap = paramMap;
            final PartnerConnection connection = getConnection(paramMap);
            instanceUrl = connection.getConfig().getServiceEndpoint().replaceFirst("/services/.*", StringUtil.EMPTY);
            bulk = BulkUtils.getBulkConnection(connection);
            mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        } catch (final AsyncApiException e) {
            throw new SalesforceDataStoreException("Failed to create a client.", e);
        }
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    private PartnerConnection getConnection(final Map<String, String> paramMap) {
        final String baseUrl = paramMap.get(BASE_URL_PARAM) != null ? paramMap.get(BASE_URL_PARAM) : BASE_URL;
        final String authType = paramMap.get(AUTH_TYPE_PARAM);
        switch(authType) {
            case OAUTH: {
                final String username = paramMap.get(USERNAME_PARAM);
                final String clientId = paramMap.get(CLIENT_ID_PARAM);
                final String privateKey = paramMap.get(PRIVATE_KEY_PARAM);
                if (username == null || clientId == null || privateKey == null) {
                    throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + CLIENT_ID_PARAM + "', '" + PRIVATE_KEY_PARAM + "'required for OAuth.");
                }
                try {
                    return AuthUtils.getConnection(username, clientId, privateKey, baseUrl);
                } catch (final ConnectionException e) {
                    throw new SalesforceDataStoreException("Failed to get connection by oauth", e);
                }
            }
            case PASSOWRD: {
                final String username = paramMap.get(USERNAME_PARAM);
                final String password = paramMap.get(PASSWORD_PARAM);
                final String securityToken = paramMap.get(SECURITY_TOKEN_PARAM);
                final String clientId = paramMap.get(CLIENT_ID_PARAM);
                final String clientSecret = paramMap.get(CLIENT_SECRET_PARAM);
                if (username == null || password == null || securityToken == null || clientId == null || clientSecret == null) {
                    throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + PASSWORD_PARAM + "', '" + SECURITY_TOKEN_PARAM +
                            "', '" + CLIENT_ID_PARAM + "', '" + CLIENT_SECRET_PARAM + "' required for Password Auth.");
                }
                try {
                    return AuthUtils.getConnectionByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
                } catch (final ConnectionException e) {
                    throw new SalesforceDataStoreException("Failed to get connection by oauth", e);
                }
            }
            default: {
                throw new SalesforceDataStoreException("parameter '" + AUTH_TYPE_PARAM + "' is required.");
            }
        }
    }

    public void getStandardObjects(final Consumer<SearchData> consumer) {
        Arrays.stream(StandardObject.values()).forEach(o -> {
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
                    logger.warn("Failed to deserialize JSON content stream as tree.", e);
                }
            });
    });
    }

    public void getCustomObjects(final Consumer<SearchData> consumer) {
        if(paramMap.get(CUSTOM_PARAM) == null) return ;
        for (String c : Arrays.stream(paramMap.get(CUSTOM_PARAM).split(",")).map(String::trim).collect(Collectors.toList())) {
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
                    logger.warn("Failed to deserialize JSON content stream as tree.", e);
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
}
