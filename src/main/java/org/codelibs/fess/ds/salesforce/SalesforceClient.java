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
import org.codelibs.fess.ds.salesforce.api.utils.AuthUtils;
import org.codelibs.fess.ds.salesforce.api.utils.BulkUtils;
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

    protected final Map<String, String> params;
    protected final BulkConnection bulk;
    protected final String instanceUrl;
    protected final ObjectMapper mapper;

    public SalesforceClient(final Map<String, String> params) {
        try {
            this.params = params;
            final PartnerConnection connection = getConnection(params);
            instanceUrl = connection.getConfig().getServiceEndpoint().replaceFirst("/services/.*", "");
            bulk = AuthUtils.getBulkConnection(connection);
            mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        } catch (final AsyncApiException e) {
            throw new SalesforceDataStoreException("Failed to create a client.", e);
        }
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    private PartnerConnection getConnection(final Map<String, String> params) {
        final String baseUrl = params.get(BASE_URL_PARAM) != null ? params.get(BASE_URL_PARAM) : BASE_URL;
        final String authType = params.get(AUTH_TYPE_PARAM);
        switch(authType) {
            case OAUTH: {
                final String username = params.get(USERNAME_PARAM);
                final String clientId = params.get(CLIENT_ID_PARAM);
                final String privateKey = params.get(PRIVATE_KEY_PARAM);
                if (username == null || clientId == null || privateKey == null) {
                    throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + CLIENT_ID_PARAM + "', '" + PRIVATE_KEY_PARAM + "' are required for OAuth.");
                }
                try {
                    return AuthUtils.getConnection(username, clientId, privateKey, baseUrl);
                } catch (Exception e) {
                    throw new SalesforceDataStoreException("Failed to get connection by OAuth.", e);
                }
            }
            case PASSOWRD: {
                final String username = params.get(USERNAME_PARAM);
                final String password = params.get(PASSWORD_PARAM);
                final String securityToken = params.get(SECURITY_TOKEN_PARAM);
                final String clientId = params.get(CLIENT_ID_PARAM);
                final String clientSecret = params.get(CLIENT_SECRET_PARAM);
                if (username == null || password == null || securityToken == null || clientId == null || clientSecret == null) {
                    throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + PASSWORD_PARAM + "', '" + SECURITY_TOKEN_PARAM +
                            "', '" + CLIENT_ID_PARAM + "', '" + CLIENT_SECRET_PARAM + "' are required for Password Auth.");
                }
                try {
                    return AuthUtils.getConnectionByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
                }catch (Exception e) {
                    throw new SalesforceDataStoreException("Failed to get connection by Password Auth.", e);
                }
            }
            default: {
                throw new SalesforceDataStoreException("parameter '" + AUTH_TYPE_PARAM + "' is required.");
            }
        }
    }

    public void getStandardObjects(final Consumer<SearchData> consumer) {
        Arrays.stream(StandardObject.values()).forEach(o -> {
            try {
                final JobInfo job = BulkUtils.createJob(bulk, o.name());
                final SearchLayout layout = getSearchLayout(o);
                final String query = BulkUtils.createQuery(o.name(), layout.fields());
                final BatchInfo batch = BulkUtils.createBatch(bulk, job, query);
                BulkUtils.getQueryResultStream(bulk, job, batch).forEach( stream -> {
                    try {
                        mapper.readTree(stream).forEach(a -> {
                            logger.info("Processsing JSON : " + a);
                            final SearchData data = new SearchData(o.name(), a, layout);
                            logger.info("got data : " + data.getContent());
                            consumer.accept(data);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (AsyncApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public void getCustomObjects(final Consumer<SearchData> consumer) {
        if(params.get(CUSTOM_PARAM) == null) return ;
        for (String c : Arrays.stream(params.get(CUSTOM_PARAM).split(",")).map(String::trim).collect(Collectors.toList())) {
            try {
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (AsyncApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private SearchLayout getSearchLayout(final StandardObject obj) {
        final String title =
                params.get(obj.name() + "." + TITLE_PARAM) != null ?
                        params.get(obj.name() + "." + TITLE_PARAM)
                        : obj.getLayout().getTitle();
        final List<String> contents =
                params.get(obj.name() + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(params.get(obj.name() + "." + CONTENTS_PARAM)
                                .split(","))
                                .map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getContents();
        final List<String> digests =
                params.get(obj.name() + "." + DIGESTS_PARAM) != null ?
                        Arrays.stream(params.get(obj.name() + "." + DIGESTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getDigests();
        final String thumbnail =
                params.get(obj.name() + "." + THUMBNAIL_PARAM) != null ?
                        params.get(obj.name() + "." + THUMBNAIL_PARAM)
                        : obj.getLayout().getThumbnail();
        return new SearchLayout(title, contents, digests, thumbnail);
    }

    private SearchLayout getSearchLayout(final String type) {
        final String title =
                params.get(type + "." + TITLE_PARAM) != null ?
                        params.get(type + "." + TITLE_PARAM)
                        : type;
        final List<String> contents =
                params.get(type + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(params.get(type + "." + CONTENTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : emptyList();
        final List<String> digests = params.get(type + "." + DIGESTS_PARAM) != null ?
                Arrays.stream(params.get(type + "." + DIGESTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                : emptyList();
        final String thumbnail = params.get(type + "." + THUMBNAIL_PARAM);
        return new SearchLayout(title, contents, digests, thumbnail);
    }
}
