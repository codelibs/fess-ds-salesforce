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

import org.codelibs.fess.es.config.exentity.DataConfig;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.codelibs.fess.util.ComponentUtil;
import org.dbflute.utflute.lastaflute.LastaFluteTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SalesforceDataStoreTest extends LastaFluteTestCase {

    private Logger logger = LoggerFactory.getLogger(SalesforceDataStoreTest.class);

    public static final String BASE_URL = "";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";
    public static final String SECURITY_TOKEN = "";
    public static final String CLIENT_ID = "";
    public static final String CLIENT_SECRET = "";
    public static final String PRIVATE_KEY = "";
    public static final long REFRESH_INTERVAL = 3600;
    public SalesforceDataStore dataStore;

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dataStore = new SalesforceDataStore();
    }

    @Override
    public void tearDown() throws Exception {
        ComponentUtil.setFessConfig(null);
        super.tearDown();
    }

    public void testStoreData() {
        // doStoreData()
    }

    private void doStoreData() {
        DataConfig dataConfig = new DataConfig();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("auth_type", "token");
        paramMap.put("username", USERNAME);
        paramMap.put("client_id", CLIENT_ID);
        paramMap.put("private_key", PRIVATE_KEY);
        paramMap.put("base_url", BASE_URL);
        FessConfig fessConfig = ComponentUtil.getFessConfig();
        Map<String, String> scriptMap = new HashMap<>();
        Map<String, Object> defaultDataMap = new HashMap<>();
        dataStore.storeData(dataConfig, new TestCallback(
            dataMap ->
            logger.debug(dataMap.get(fessConfig.getIndexFieldTitle()).toString()))
        , paramMap, scriptMap, defaultDataMap);
    }

}
