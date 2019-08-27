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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.async.BatchInfo;
import com.sforce.async.BulkConnection;
import com.sforce.async.JobInfo;
import com.sforce.soap.partner.PartnerConnection;
import org.codelibs.fess.ds.AbstractDataStore;
import org.codelibs.fess.ds.callback.IndexUpdateCallback;
import org.codelibs.fess.ds.salesforce.api.*;
import org.codelibs.fess.ds.salesforce.api.sobject.StandardObject;
import org.codelibs.fess.es.config.exentity.DataConfig;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.codelibs.fess.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class SalesforceDataStore extends AbstractDataStore {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceDataStore.class);

    protected static String BASE_URL = "https://login.salesforce.com";

    // parameters
    protected static final String BASE_URL_PARAM = "base_url";
    protected static final String AUTH_TYPE_PARAM = "auth_type";
    protected static final String USERNAME_PARAM = "username";
    protected static final String PASSWORD_PARAM = "password";
    protected static final String SECURITY_TOKEN_PARAM = "security_token";
    protected static final String CLIENT_ID_PARAM = "client_id";
    protected static final String  CLIENT_SECRET_PARAM = "client_secret";
    protected static final String  PRIVATE_KEY_PARAM = "private_key";

    protected static final String  TITLE_PARAM = "title";
    protected static final String  CONTENTS_PARAM = "contents";
    protected static final String  DIGESTS_PARAM = "digests";
    protected static final String  THUMBNAIL_PARAM = "thumbnail";
    protected static final String  CUSTOM_PARAM = "custom";

    @Override
    public String getName() {
        return "Salesforce";
    }

    @Override
    public void storeData(DataConfig dataConfig, IndexUpdateCallback callback, Map<String, String> paramMap,
                                  Map<String, String> scriptMap, Map<String, Object> defaultDataMap) {
        try {
            PartnerConnection connection = getConnection(paramMap);

            String instanceUrl = connection.getConfig().getServiceEndpoint().replaceFirst("/services/.*", "");
            BulkConnection bulk = Authentications.getBulkConnection(connection);
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            storeStandardObjects(bulk, mapper, callback, paramMap, scriptMap, defaultDataMap, instanceUrl);
            storeCustomObjects(bulk, mapper, callback, paramMap, scriptMap, defaultDataMap, instanceUrl);
        } catch (Exception e) {
            // TODO
            logger.error(e.getMessage(), e);
            return ;
        }
    }

    private void storeStandardObjects(BulkConnection bulk, ObjectMapper mapper,
                                     IndexUpdateCallback callback, Map<String, String> paramMap,
                                      Map<String, String> scriptMap,
                                      Map<String, Object> defaultDataMap, String instanceUrl) {
        Arrays.stream(StandardObject.values()).forEach( o -> {
            try {
                    JobInfo job = Bulks.createJob(bulk, o.name());
                    SearchLayout layout = getSearchLayout(paramMap, o);
                    String query = Bulks.createQuery(o.name(), layout.fields());
                    BatchInfo batch = Bulks.createBatch(bulk, job, query);
                        Bulks.getQueryResultStream(bulk, job, batch).forEach(stream -> {
                                try {
                                    mapper.readTree(stream).forEach(a -> {
                                        SearchData data = new SearchData(o.name(), a, layout);
                                        storeSObjectData(callback, paramMap, scriptMap, defaultDataMap, data, instanceUrl);
                                    });
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    // TODO
                                }
                        });
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        // TODO
                    }
                });
    }

    private void storeCustomObjects(BulkConnection bulk, ObjectMapper mapper,
                                   IndexUpdateCallback callback, Map<String, String> paramMap,
                                    Map<String, String> scriptMap, Map<String, Object> defaultDataMap,
                                   String instanceUrl) {
        getCustomObjects(paramMap).stream().forEach( c -> {
            try {
            JobInfo job = Bulks.createJob(bulk, c);
            SearchLayout layout = getSearchLayout(paramMap, c);
            String query = Bulks.createQuery(c, layout.fields());
            BatchInfo batch = Bulks.createBatch(bulk, job, query);
                Bulks.getQueryResultStream(bulk, job, batch).forEach(stream -> {
                    try {
                        mapper.readTree(stream).forEach(a -> {
                            SearchData data = new SearchData(c, a, layout);
                            storeSObjectData(callback, paramMap, scriptMap, defaultDataMap, data, instanceUrl);
                        });
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        // TODO
                    }
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                // TODO
            }
        });
    }

    private void storeSObjectData(IndexUpdateCallback callback, Map<String, String> paramMap,
                             Map<String, String> scriptMap, Map<String, Object> defaultDataMap,
                                 SearchData data, String instanceUrl) {
        FessConfig fessConfig = ComponentUtil.getFessConfig();
        Map<String, Object> dataMap = new HashMap(defaultDataMap);
        dataMap.put(fessConfig.getIndexFieldTitle(), "[" + data.getType() + "] " + data.getTitle());

        dataMap.put(fessConfig.getIndexFieldContent(), data.getContent());
        dataMap.put(fessConfig.getIndexFieldContentLength(), data.getContent().length());
        dataMap.put(fessConfig.getIndexFieldDigest(), data.getDigest());
        dataMap.put(fessConfig.getIndexFieldCreated(), data.getCreated());
        dataMap.put(fessConfig.getIndexFieldLastModified(), data.getLastModified());
        dataMap.put(fessConfig.getIndexFieldUrl(), instanceUrl + "/" + data.getId());
        dataMap.put(fessConfig.getIndexFieldThumbnail(), data.getThumbnail());
        callback.store(paramMap, dataMap);
    }

    private PartnerConnection getConnection(Map<String, String> paramMap) {
        String baseUrl = paramMap.get(BASE_URL_PARAM) != null ? paramMap.get(BASE_URL_PARAM) : BASE_URL;
        String authType = paramMap.get(AUTH_TYPE_PARAM);
        switch(authType) {
            case "oauth": {
                String username = paramMap.get(USERNAME_PARAM);
                String clientId = paramMap.get(CLIENT_ID_PARAM);
                String privateKey = paramMap.get(PRIVATE_KEY_PARAM);
                if (username == null || clientId == null || privateKey == null) {
                    throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + CLIENT_ID_PARAM + "', '" + PRIVATE_KEY_PARAM + "' are required for OAuth.");
                }
                // TODO
                logger.info("[username=" + username + ",cliendId=" + clientId + ",privateKey=" + privateKey + "]");
                try {
                    return Authentications.getConnection(username, clientId, privateKey, baseUrl);
                } catch (Exception e) {
                    throw new SalesforceDataStoreException("Failed to get connection.", e);
                }
            }
            case "password": {
                String username = paramMap.get(USERNAME_PARAM);
                String password = paramMap.get(PASSWORD_PARAM);
                String securityToken = paramMap.get(SECURITY_TOKEN_PARAM);
                String clientId = paramMap.get(CLIENT_ID_PARAM);
                String clientSecret = paramMap.get(CLIENT_SECRET_PARAM);
                if (username == null || password == null || securityToken == null || clientId == null || clientSecret == null) {
                    throw new SalesforceDataStoreException("parameters '" + USERNAME_PARAM + "', '" + PASSWORD_PARAM + "', '" + SECURITY_TOKEN_PARAM +
                            "', '" + CLIENT_ID_PARAM + "', '" + CLIENT_SECRET_PARAM + "' are required for Password Auth.");
                }
                try {
                    return Authentications.getConnectionByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
                }catch (Exception e) {
                    throw new SalesforceDataStoreException("Failed to get connection by password.", e);
                }
            }
            default: {
                throw new SalesforceDataStoreException("parameter '" + AUTH_TYPE_PARAM + "' is required.");
            }
        }
    }

    private SearchLayout getSearchLayout(Map<String, String> paramMap, StandardObject obj) {
        return new SearchLayout(
                paramMap.get(obj.name() + "." + TITLE_PARAM)  != null ?
                        paramMap.get(obj.name() + "." + TITLE_PARAM)
                        : obj.getLayout().getTitle(),
                paramMap.get(obj.name() + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(obj.name() + "." + CONTENTS_PARAM)
                                .split(","))
                        .map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getContents(),
                paramMap.get(obj.name() + "." + DIGESTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(obj.name() + "." + DIGESTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : obj.getLayout().getDigests(),
                paramMap.get(obj.name() + "." + THUMBNAIL_PARAM) != null ?
                        paramMap.get(obj.name() + "." + THUMBNAIL_PARAM)
            : obj.getLayout().getThumbnail());
    }

    private SearchLayout getSearchLayout(Map<String, String> paramMap, String type) {
        return new SearchLayout(
                paramMap.get(type + "." + TITLE_PARAM) != null ?
                        paramMap.get(type + "." + TITLE_PARAM)
                        : type,
                paramMap.get(type + "." + CONTENTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(type + "." + CONTENTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : emptyList(),
                paramMap.get(type + "." + DIGESTS_PARAM) != null ?
                        Arrays.stream(paramMap.get(type + "." + DIGESTS_PARAM).split(",")).map(String::trim).collect(Collectors.toList())
                        : emptyList(),
                paramMap.get(type + "." + THUMBNAIL_PARAM));
    }

    private List<String> getCustomObjects(Map<String, String> paramMap) {
        return paramMap.get(CUSTOM_PARAM) != null ?
                Arrays.stream(paramMap.get(CUSTOM_PARAM).split(",")).map(String::trim).collect(Collectors.toList()) :
                emptyList();
    }

}
