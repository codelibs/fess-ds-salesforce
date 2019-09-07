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

import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.codelibs.fess.ds.salesforce.api.SearchLayout;
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.codelibs.fess.ds.salesforce.api.sobject.StandardObject;
import org.codelibs.fess.ds.salesforce.SalesforceClient;
import org.codelibs.fess.ds.salesforce.SalesforceClient.ConnectionProvider;
import org.dbflute.utflute.lastaflute.LastaFluteTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.codelibs.fess.ds.salesforce.SalesforceClient.API_VERSION;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.AUTH_TYPE_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.BASE_URL_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.CLIENT_ID_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.PRIVATE_KEY_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.USERNAME_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.getTokenResponseByToken;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.getTokenResponseByPass;
import static org.codelibs.fess.ds.salesforce.SalesforceDataStoreTest.*;

public class SalesforceClientTest extends LastaFluteTestCase {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceClientTest.class);

    public static final String BASE_URL= "https://login.salesforce.com";
    public static final String AUTH_TYPE = "token";
    public static final String USERNAME = "sforce@example.com";
    public static final String CLIENT_ID = "...";
    public static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----...-----END PRIVATE KEY-----";
    public static final long REFRESH_INTERVAL = 0;

    Map<String, String> params;
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
        params = new HashMap<>();
        params.put(BASE_URL_PARAM, BASE_URL);
        params.put(AUTH_TYPE_PARAM, AUTH_TYPE);
        params.put(USERNAME_PARAM, USERNAME);
        params.put(CLIENT_ID_PARAM, CLIENT_ID);
        params.put(PRIVATE_KEY_PARAM, PRIVATE_KEY);
        // client = new SalesforceClient(params); TODO
    }

    public void test_parseTokenResponse() {
        String json = "{ \"id\":\"https://login.salesforce.com/id/00Dx0000000BV7z/testId\",\n" +
                "\"issued_at\":\"1278448384422\",\"instance_url\":\"https://testInstance.salesforce.com/\",\n" +
                "\"signature\":\"test_signature\"," +
                "\"access_token\":\"test_token\",\"token_type\":\"Bearer\",\"scope\":\"id api\"}";
        TokenResponse response = SalesforceClient.parseTokenResponse(new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8"))));
        assertEquals("test_token", response.getAccessToken());
    }

    public void testGetSearchLayout() {
        // TODO
        // doGetSearchLayout();
    }

    public void doGetSearchLayout() {
        final StandardObject asset = StandardObject.Asset;
        final SearchLayout layout = client.getSearchLayout(asset);
        assertEquals(asset.getLayout().getTitle(), layout.getTitle());
        assertEquals(asset.getLayout().getContents(), layout.getContents());
    }

    public void testConnectionProvider() {
        try {
            connectionProvider = new ConnectionProvider(params);
        } catch (Exception e) {
            // fail(e.getMessage());
        }
        // TODO
        // doGetConnection(connectionProvider);
        // doRefreshConnection(connectionProvider);
        // doRefreshConnection(connectionProvider);
        // doGetTokenResponseByToken(connectionProvider);
        // doGetTokenResponseByPassword(connectionProvider);
        // doGetConnectionByToken(connectionProvider);
        // doGetConnectionByPassword(connectionProvider);
        // doCreateConfig(connectionProvider);

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
            TokenResponse response = getTokenResponseByToken(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, REFRESH_INTERVAL);
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    protected void doGetTokenResponseByPass(final ConnectionProvider connectionProvider) {
        try {
            TokenResponse response = getTokenResponseByPass(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    protected void doGetConnectionByToken(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnectionByToken(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, REFRESH_INTERVAL);
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    protected void doGetConnectionByPassword(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnectionByPass(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    protected void doCreateConfig(final ConnectionProvider connectionProvider) {
        try {

            String json = "{ \"id\":\"https://login.salesforce.com/id/test/testId\",\n" +
                    "\"issued_at\":\"1278448384422\",\"instance_url\":\"https://testInstance.salesforce.com/\",\n" +
                    "\"signature\":\"test_signature\"," +
                    "\"access_token\":\"test_token\",\"token_type\":\"Bearer\",\"scope\":\"id api\"}";
            TokenResponse response = SalesforceClient.parseTokenResponse(new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8"))));
            ConnectorConfig config = connectionProvider.createConnectorConfig(response);
            assertEquals("test_token", config.getSessionId());
            assertEquals("https://testInstance.salesforce.com/services/Soap/u/" + API_VERSION, config.getAuthEndpoint());
            assertEquals("https://testInstance.salesforce.com/services/Soap/u/" + API_VERSION + "/https://login.salesforce.com/id/test/testId", config.getAuthEndpoint());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
