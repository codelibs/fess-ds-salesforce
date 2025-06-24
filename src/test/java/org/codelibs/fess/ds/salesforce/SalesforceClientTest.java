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

import static org.codelibs.fess.ds.salesforce.SalesforceClient.API_VERSION;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.AUTH_TYPE_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.BASE_URL_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.CLIENT_ID_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.PRIVATE_KEY_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.USERNAME_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.convertSnakeToCamel;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.parseTokenResponse;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codelibs.fess.ds.salesforce.SalesforceClient.ConnectionProvider;
import org.codelibs.fess.ds.salesforce.api.SearchLayout;
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.codelibs.fess.ds.salesforce.api.sobject.StandardObject;
import org.codelibs.fess.entity.DataStoreParams;
import org.dbflute.utflute.lastaflute.LastaFluteTestCase;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class SalesforceClientTest extends LastaFluteTestCase {

    private static final Logger logger = LogManager.getLogger(SalesforceClientTest.class);

    public static final String BASE_URL = "https://login.salesforce.com";
    public static final String AUTH_TYPE = "token";
    public static final String USERNAME = "sforce@example.com";
    public static final String CLIENT_ID = "...";
    public static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----...-----END PRIVATE KEY-----";
    public static final long REFRESH_INTERVAL = 0;

    DataStoreParams paramMap;
    SalesforceClient client;
    ConnectionProvider connectionProvider;

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
        paramMap = new DataStoreParams();
        paramMap.put(BASE_URL_PARAM, BASE_URL);
        paramMap.put(AUTH_TYPE_PARAM, AUTH_TYPE);
        paramMap.put(USERNAME_PARAM, USERNAME);
        paramMap.put(CLIENT_ID_PARAM, CLIENT_ID);
        paramMap.put(PRIVATE_KEY_PARAM, PRIVATE_KEY);
        // client = new SalesforceClient(paramMap); TODO
    }

    public void testGetSearchLayout() {
        // TODO
        // doGetSearchLayout();
    }

    public void doGetSearchLayout() {
        final StandardObject asset = StandardObject.ASSET;
        final SearchLayout layout = client.getSearchLayout(asset);
        assertEquals(asset.getLayout().getTitle(), layout.getTitle());
        assertEquals(asset.getLayout().getContents(), layout.getContents());
    }

    public void testConnectionProvider() {
        try {
            connectionProvider = new ConnectionProvider(paramMap);
        } catch (Exception e) {
            // fail(e.getMessage());
        }
        // TODO
        // doGetConnection(connectionProvider);
        // doRefreshConnection(connectionProvider);
        // doRefreshConnection(connectionProvider);
        // doGetTokenResponseByToken(connectionProvider);
        // doGetTokenResponseByPass(connectionProvider);
        // doGetConnectionByToken(connectionProvider);
        // doGetConnectionByPassword(connectionProvider);
        // doCreateConfig(connectionProvider();
    }

    protected void doGetConnection(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnection();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    protected void doRefreshConnection(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.refreshConnection();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    protected void doGetTokenResponseByToken(final ConnectionProvider connectionProvider) {
        try {
            TokenResponse response = connectionProvider.getTokenResponseByToken();
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    protected void doGetTokenResponseByPass() {
        try {
            TokenResponse response = connectionProvider.getTokenResponseByPass();
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    protected void doGetConnectionByToken(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnectionByToken();
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    protected void doGetConnectionByPassword(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnectionByPass();
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    protected void doCreateConfig(final ConnectionProvider connectionProvider) {
        try {

            String json = "{ \"id\":\"https://login.salesforce.com/id/test/testId\",\n"
                    + "\"issued_at\":\"1278448384422\",\"instance_url\":\"https://testInstance.salesforce.com/\",\n"
                    + "\"signature\":\"test_signature\","
                    + "\"access_token\":\"test_token\",\"token_type\":\"Bearer\",\"scope\":\"id api\"}";
            TokenResponse response = parseTokenResponse(new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8"))));
            ConnectorConfig config = connectionProvider.createConnectorConfig(response);
            assertEquals("test_token", config.getSessionId());
            assertEquals("https://testInstance.salesforce.com/services/Soap/u/" + API_VERSION, config.getAuthEndpoint());
            assertEquals(
                    "https://testInstance.salesforce.com/services/Soap/u/" + API_VERSION + "/https://login.salesforce.com/id/test/testId",
                    config.getAuthEndpoint());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void test_ParseTokenResponse() {
        String json = "{ \"id\":\"https://login.salesforce.com/id/00Dx0000000BV7z/testId\",\n"
                + "\"issued_at\":\"1278448384422\",\"instance_url\":\"https://testInstance.salesforce.com/\",\n"
                + "\"signature\":\"test_signature\"," + "\"access_token\":\"test_token\",\"token_type\":\"Bearer\",\"scope\":\"id api\"}";
        TokenResponse response = parseTokenResponse(new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8"))));
        assertEquals("test_token", response.getAccessToken());
    }

    public void test_convertSnakeToCamel() {
        assertEquals("DandBCompany", convertSnakeToCamel("DAND_B_COMPANY"));
        assertEquals("CollaborationGroup", convertSnakeToCamel("COLLABORATION_GROUP"));
    }

}
