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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codelibs.fess.Constants;
import org.codelibs.fess.app.service.FailureUrlService;
import org.codelibs.fess.ds.callback.IndexUpdateCallback;
import org.codelibs.fess.ds.salesforce.api.SearchData;
import org.codelibs.fess.ds.salesforce.api.SearchLayout;
import org.codelibs.fess.entity.DataStoreParams;
import org.codelibs.fess.helper.CrawlerStatsHelper;
import org.codelibs.fess.helper.CrawlerStatsHelper.StatsKeyObject;
import org.codelibs.fess.helper.SystemHelper;
import org.codelibs.fess.opensearch.config.exentity.CrawlingConfig;
import org.codelibs.fess.opensearch.config.exentity.DataConfig;
import org.codelibs.fess.opensearch.config.exentity.FailureUrl;
import org.codelibs.fess.script.ScriptEngineFactory;
import org.codelibs.fess.util.ComponentUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.objenesis.ObjenesisStd;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Pins where the per-document {@link StatsKeyObject} lives.
 *
 * <p>
 * {@link Constants#CRAWLER_STATS_KEY} identifies one document for statistics and logging. It must
 * not be written to the {@link DataStoreParams} that every worker thread shares, because another
 * worker can overwrite it between the write and the {@code callback.store} that reads it back.
 * These tests assert exact properties (the shared map is never written to, each document gets its
 * own instance) rather than reproducing the race, which is unreliable.
 * </p>
 */
public class SalesforceDataStoreStatsKeyTest extends UnitDsTestCase {

    private static final String INSTANCE_URL = "https://example.my.salesforce.com";

    private SalesforceDataStore dataStore;

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }

    @Override
    public void setUp(final TestInfo testInfo) throws Exception {
        super.setUp(testInfo);
        dataStore = new SalesforceDataStore();

        ComponentUtil.register(new SystemHelper(), "systemHelper");
        final CrawlerStatsHelper crawlerStatsHelper = new CrawlerStatsHelper();
        crawlerStatsHelper.init();
        ComponentUtil.register(crawlerStatsHelper, "crawlerStatsHelper");

        ComponentUtil.register(new FailureUrlService() {
            @Override
            public FailureUrl store(final CrawlingConfig crawlingConfig, final String errorName, final String url, final Throwable e) {
                return null;
            }
        }, FailureUrlService.class.getCanonicalName());

        // Resolves a template as a dot path into the script scope; a verbatim key match is handled
        // by AbstractDataStore#convertValue before the engine is consulted.
        final ScriptEngineFactory scriptEngineFactory = new ScriptEngineFactory();
        scriptEngineFactory.add("groovy", (template, resultMap) -> {
            Object value = resultMap;
            for (final String part : template.split("\\.")) {
                if (!(value instanceof Map)) {
                    return null;
                }
                value = ((Map<?, ?>) value).get(part);
            }
            return value;
        });
        ComponentUtil.register(scriptEngineFactory, "scriptEngineFactory");
    }

    @Override
    public void tearDown(final TestInfo testInfo) throws Exception {
        ComponentUtil.setFessConfig(null);
        super.tearDown(testInfo);
    }

    private SalesforceClient newClient() throws Exception {
        // The constructor connects to Salesforce; only getInstanceUrl() is needed here.
        final SalesforceClient client = new ObjenesisStd().newInstance(SalesforceClient.class);
        final Field field = SalesforceClient.class.getDeclaredField("instanceUrl");
        field.setAccessible(true);
        field.set(client, INSTANCE_URL);
        return client;
    }

    private SearchData newSearchData(final String id) throws Exception {
        final String json = "{\"Id\":\"" + id + "\",\"Name\":\"name-" + id + "\",\"CreatedDate\":1,\"LastModifiedDate\":2}";
        return new SearchData("Account", new ObjectMapper().readTree(json), new SearchLayout("Name", null, null, null));
    }

    private static class RecordingCallback implements IndexUpdateCallback {
        final List<DataStoreParams> paramMaps = new ArrayList<>();
        final List<Map<String, Object>> dataMaps = new ArrayList<>();

        @Override
        public void store(final DataStoreParams paramMap, final Map<String, Object> dataMap) {
            paramMaps.add(paramMap);
            dataMaps.add(dataMap);
        }

        @Override
        public long getDocumentSize() {
            return paramMaps.size();
        }

        @Override
        public long getExecuteTime() {
            return 0;
        }

        @Override
        public void commit() {
        }
    }

    private DataStoreParams newParamMap() {
        final DataStoreParams paramMap = new DataStoreParams();
        paramMap.put("username", "user@example.com");
        return paramMap;
    }

    /**
     * The shared map is never written to, and each stored document arrives with its own copy that
     * carries its own stats key plus the ordinary parameters.
     */
    @Test
    public void test_eachDocumentGetsItsOwnParamsAndTheSharedMapIsUntouched() throws Exception {
        final DataStoreParams paramMap = newParamMap();
        final RecordingCallback callback = new RecordingCallback();
        final SalesforceClient client = newClient();
        final Map<String, String> scriptMap = new HashMap<>();
        scriptMap.put("url", "object.url");

        for (final String id : new String[] { "001A", "001B" }) {
            dataStore.processSearchData(new DataConfig(), callback, new HashMap<>(), paramMap, scriptMap, new HashMap<>(),
                    newSearchData(id), client);
        }

        assertNull("the stats key must never be written to the map the worker threads share", paramMap.get(Constants.CRAWLER_STATS_KEY));

        assertEquals("every document must reach the callback", 2, callback.paramMaps.size());
        assertNotSame("the two documents must not share one instance", callback.paramMaps.get(0), callback.paramMaps.get(1));
        final String[] expectedIds = { "001A", "001B" };
        for (int i = 0; i < 2; i++) {
            final DataStoreParams localParams = callback.paramMaps.get(i);
            assertNotSame("callback.store must receive the copy, not the shared instance", paramMap, localParams);
            final Object value = localParams.get(Constants.CRAWLER_STATS_KEY);
            assertTrue("the copy must still carry the stats key the callback contract expects", value instanceof StatsKeyObject);
            assertEquals("the stats key must identify this document", INSTANCE_URL + "/" + expectedIds[i],
                    ((StatsKeyObject) value).getId());
            assertEquals("the copy must carry the ordinary parameters too", "user@example.com", localParams.getAsString("username"));
        }
    }

    /**
     * A script template naming the stats key finds nothing, since the script scope is seeded from
     * the shared map that no longer holds it.
     */
    @Test
    public void test_statsKeyDoesNotReachTheScriptScope() throws Exception {
        final RecordingCallback callback = new RecordingCallback();
        final Map<String, String> scriptMap = new HashMap<>();
        scriptMap.put("stats_leak", Constants.CRAWLER_STATS_KEY);

        dataStore.processSearchData(new DataConfig(), callback, new HashMap<>(), newParamMap(), scriptMap, new HashMap<>(),
                newSearchData("001A"), newClient());

        assertEquals("the document must reach the callback", 1, callback.dataMaps.size());
        assertNull("internal crawl plumbing must not be indexable", callback.dataMaps.get(0).get("stats_leak"));
    }
}
