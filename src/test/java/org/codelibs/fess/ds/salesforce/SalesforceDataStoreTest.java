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

import org.junit.jupiter.api.TestInfo;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codelibs.fess.entity.DataStoreParams;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.codelibs.fess.opensearch.config.exentity.DataConfig;
import org.codelibs.fess.util.ComponentUtil;
import org.codelibs.fess.ds.salesforce.UnitDsTestCase;

public class SalesforceDataStoreTest extends UnitDsTestCase {

    private Logger logger = LogManager.getLogger(SalesforceDataStoreTest.class);

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
    public void setUp(TestInfo testInfo) throws Exception {
        super.setUp(testInfo);
        dataStore = new SalesforceDataStore();
    }

    @Override
    public void tearDown(TestInfo testInfo) throws Exception {
        ComponentUtil.setFessConfig(null);
        super.tearDown(testInfo);
    }

    public void test_getName() {
        // Act
        String name = dataStore.getName();

        // Assert
        assertEquals("SalesforceDataStore", name);
    }

    public void test_isIgnoreError_withTrue() {
        // Arrange
        DataStoreParams paramMap = new DataStoreParams();
        paramMap.put("ignore_error", "true");

        // Act
        boolean result = dataStore.isIgnoreError(paramMap);

        // Assert
        assertTrue(result);
    }

    public void test_isIgnoreError_withFalse() {
        // Arrange
        DataStoreParams paramMap = new DataStoreParams();
        paramMap.put("ignore_error", "false");

        // Act
        boolean result = dataStore.isIgnoreError(paramMap);

        // Assert
        assertFalse(result);
    }

    public void test_isIgnoreError_withDefaultValue() {
        // Arrange
        DataStoreParams paramMap = new DataStoreParams();
        // Not setting ignore_error parameter

        // Act
        boolean result = dataStore.isIgnoreError(paramMap);

        // Assert
        assertTrue(result); // Default is true
    }

    public void test_newFixedThreadPool_withSingleThread() {
        // Act
        java.util.concurrent.ExecutorService executor = dataStore.newFixedThreadPool(1);

        // Assert
        assertNotNull(executor);
        assertFalse(executor.isShutdown());

        // Cleanup
        executor.shutdown();
    }

    public void test_newFixedThreadPool_withMultipleThreads() {
        // Act
        java.util.concurrent.ExecutorService executor = dataStore.newFixedThreadPool(5);

        // Assert
        assertNotNull(executor);
        assertFalse(executor.isShutdown());

        // Cleanup
        executor.shutdown();
    }

    public void test_createClient() {
        // Arrange
        DataStoreParams paramMap = new DataStoreParams();
        paramMap.put("auth_type", "oauth_token");
        paramMap.put("username", "test@example.com");
        paramMap.put("client_id", "test_client_id");
        paramMap.put("private_key", "test_key");
        paramMap.put("base_url", BASE_URL);

        // Act & Assert
        // Note: This will fail without valid credentials, but we can verify it attempts to create a client
        try {
            SalesforceClient client = dataStore.createClient(paramMap);
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            // Expected when credentials are not valid
            assertTrue(e instanceof org.codelibs.fess.ds.salesforce.SalesforceDataStoreException);
        }
    }

    public void testStoreData() {
        // Note: This test requires valid Salesforce credentials and connection
        // Uncomment the line below to run the actual integration test
        // doStoreData()
    }

    private void doStoreData() {
        DataConfig dataConfig = new DataConfig();
        DataStoreParams paramMap = new DataStoreParams();
        paramMap.put("auth_type", "token");
        paramMap.put("username", USERNAME);
        paramMap.put("client_id", CLIENT_ID);
        paramMap.put("private_key", PRIVATE_KEY);
        paramMap.put("base_url", BASE_URL);
        FessConfig fessConfig = ComponentUtil.getFessConfig();
        Map<String, String> scriptMap = new HashMap<>();
        Map<String, Object> defaultDataMap = new HashMap<>();
        dataStore.storeData(dataConfig, new TestCallback(dataMap -> logger.debug(dataMap.get(fessConfig.getIndexFieldTitle()).toString())),
                paramMap, scriptMap, defaultDataMap);
    }

}
