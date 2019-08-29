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

import java.util.HashMap;
import java.util.Map;

import org.codelibs.fess.ds.AbstractDataStore;
import org.codelibs.fess.ds.callback.IndexUpdateCallback;
import org.codelibs.fess.ds.salesforce.api.SearchData;
import org.codelibs.fess.es.config.exentity.DataConfig;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.codelibs.fess.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesforceDataStore extends AbstractDataStore {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceDataStore.class);

    protected static String BASE_URL = "https://login.salesforce.com";

    @Override
    public String getName() {
        return "Salesforce";
    }

    @Override
    public void storeData(final DataConfig dataConfig, final IndexUpdateCallback callback, final Map<String, String> params,
                          final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap) {
        final SalesforceClient client = createClient(params);
        storeStandardObjects(callback, params, scriptMap, defaultDataMap, client);
        storeCustomObjects(callback, params, scriptMap, defaultDataMap, client);
    }

    private void storeStandardObjects(final IndexUpdateCallback callback,
                                      final Map<String, String> params, final Map<String, String> scriptMap,
                                      final Map<String, Object> defaultDataMap, final SalesforceClient client) {
        client.getStandardObjects(a -> processSearchData(callback, params, scriptMap, defaultDataMap, a, client));
    }

    private void storeCustomObjects(final IndexUpdateCallback callback, final Map<String, String> params,
                                    final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap,
                                    final SalesforceClient client) {
        client.getCustomObjects(a -> processSearchData(callback, params, scriptMap, defaultDataMap, a, client));
    }

    private void processSearchData(final IndexUpdateCallback callback, final Map<String, String> params,
                                   final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap,
                                   final SearchData data, final SalesforceClient client) {
        final FessConfig fessConfig = ComponentUtil.getFessConfig();
        final Map<String, Object> dataMap = new HashMap(defaultDataMap);

        dataMap.put(fessConfig.getIndexFieldTitle(), "[" + data.getType() + "] " + data.getTitle());
        dataMap.put(fessConfig.getIndexFieldContent(), data.getContent());
        dataMap.put(fessConfig.getIndexFieldContentLength(), data.getContent().length());
        dataMap.put(fessConfig.getIndexFieldDigest(), data.getDigest());
        dataMap.put(fessConfig.getIndexFieldCreated(), data.getCreated());
        dataMap.put(fessConfig.getIndexFieldLastModified(), data.getLastModified());
        dataMap.put(fessConfig.getIndexFieldUrl(), client.getInstanceUrl() + "/" + data.getId());
        dataMap.put(fessConfig.getIndexFieldThumbnail(), data.getThumbnail());

        if (logger.isDebugEnabled()) {
            logger.debug("dataMap: {}", dataMap);
        }

        callback.store(params, dataMap);
    }

    protected SalesforceClient createClient(final Map<String, String> paramMap) {
        return new SalesforceClient(paramMap);
    }
}
