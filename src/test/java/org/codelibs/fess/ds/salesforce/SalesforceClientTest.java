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
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.dbflute.utflute.lastaflute.LastaFluteTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.codelibs.fess.ds.salesforce.SalesforceClient.ConnectionProvider;

import static org.codelibs.fess.ds.salesforce.SalesforceClient.API_VERSION;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.AUTH_TYPE_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.BASE_URL_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.CLIENT_ID_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.PRIVATE_KEY_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceClient.USERNAME_PARAM;
import static org.codelibs.fess.ds.salesforce.SalesforceDataStoreTest.*;

public class SalesforceClientTest extends LastaFluteTestCase {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceClientTest.class);

    public static final String BASE_URL= "https://login.salesforce.com";
    public static final String AUTH_TYPE = "token";
    public static final String USERNAME = "sforce@example.com";
    public static final String CLIENT_ID = "3MVG9G9pzCUSkzZvAOi1RgHl3R.34nTLefhmRBQ7Zsb9uPLdOXpas_ubQn6uzeEeEgURuD.FrGOLW3lAlKn97";
    public static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDwL6Vfv02KBd6OfAlaH3DDVtVeeMplLAzJPOFL/up173BVq7aGUtiyz61i+itj9QkS+in9l84nyjUZQzq5nFE9/s72flpJKMZc5kiC0m+JzQLMybR2/qK5da3S4IzXDeqmP/uczTM9iplBQFxqJ+48Vz+vdI/4Tx8j8e9iTIUB9KG13RtM9dfBCB/LUq2R274gL9GTnirZpiZoL/NTcG5aWNKlVxDnqnTEYHzqws6oU27E4rY64FAMwLPcofDBut9E5cPWfPPwtdbTrjR7kyLVYBlCWNiHPvz7RKMUvbHE9PlA1aPJ7d7vKcCO9ENqXoyvgo0PXnWbzqcCZ2fDilntAgMBAAECggEBAKMkidzOUTm2IOSBRczsXCiiu41O2JL957Vs389B2DnBKHlYiEMW1NAoFiqLqJtdngtA1vLEgSgvxf9h1eqrTdehUyzEyEi3JH2HgasyisZ79THqs+S7swXr8+Sv15pffonsHdj03KApm01iDSOh+cUMslpX/053V7yPob0QIqwfEC1gEM77toxtrfZyacTWH9tblFgTfR0i/5X9+O97KluqqQ5SZGWoJ4yvCWMl+vjFjEY4W4yUohKh7EGaNFU2A8ZD1I6r8LJH71W0OA6M89SmdMKXAHlivsUcWLrkTwpYlRZ9QA7pu16jpTOGq2kqMziTWCpoOzNK0pGLEYTdF9UCgYEA+95QOPgR4QF5JoiV23z9ziIpdb2Rl6fiUBxjuyjrm+EFFQH4VPJOnrk28NHDI3H4D6QicP7eOPqOvatWL0MjgYVkEX7FcmU9WvCq4f5qQcbN2vilNZMB34nTL2GdCzfdRfrkzQZMag9gPwpVKgLOcoMS4BK204Njeerp3G+i9S8CgYEA9CBGQbE3u9XmOT/5ma/h3H/zpal7GBfqB7yC9xkfMw+Ol02PCZJn9S98t0elzJvriL4Y1FVrkxfDFG14at9b8sNVToQiOQrU37Zjh6gy+zDv0yqKAukttSEBTKTpQjNP6ZzedjTEzjY4/kPTwSt91dNl46UGyhKKh9sLi1zWU6MCgYEAhkSgbY5JMbLwW8tqYATE5LOveHXjfH5iFiTcQbTxzTpq1CUltlsp8FF6aWzZYzbpb6UnOyeTXbqsh44kNrPK5MwwaWc0aORxjd5IqotPJ9uMeBQfNm01DW7S5ypZZtaUHi8+89FMwSmLPHAMsIWoesFHOa6gSid7y02g+AOHUr8CgYEAt4nsSEdsl5PhWvl2Ns29CYJJNCuPmDWihd58uDny8vinQ6nT+GZSMkxZf5ImXQZ8tnn1QO5Xymb7C8ih1/fFsWaaJEXDVQ+HrAn8GmmiqqfIJwK8cCPPcXY2++CgXl0ln4WI22Yg8MhjYQatlXWVTcV5vQS9bf8yl6FftI8/s/ECgYEAlrxTxCjXET5ZxNR2tDeMPfcdMQo2vmndx14bb202JAp2ZGQRRoPVhnSeDUzt2UzVClqm9GxUhPxRO0/rkzM5Gp+BsToLCzI0WuT393BJ9Ma7x/uZR40FuA3rg/hfKTW0zxs0tug3U8ZICY980dkzYGRCZvbu8yJ543JyfYE9w/s=-----END PRIVATE KEY-----";
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
        // client = new SalesforceClient(params);
    }

    public void test_parseTokenResponse() {
        String json = "{ \"id\":\"https://login.salesforce.com/id/00Dx0000000BV7z/testId\",\n" +
                "\"issued_at\":\"1278448384422\",\"instance_url\":\"https://testInstance.salesforce.com/\",\n" +
                "\"signature\":\"test_signature\"," +
                "\"access_token\":\"test_token\",\"token_type\":\"Bearer\",\"scope\":\"id api\"}";
        TokenResponse response = SalesforceClient.parseTokenResponse(new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8"))));
        assertEquals("test_token", response.getAccessToken());
    }

    public void testConnectionProvider() {
        try {
            connectionProvider = new ConnectionProvider(params);
        } catch (Exception e) {
            // fail(e.getMessage());
        }
        // doGetConnection(connectionProvider);
        // doRefreshConnection(connectionProvider);
        // doRefreshConnection(connectionProvider);
        // doGetTokenResponseByToken(connectionProvider);
        // doGetTokenResponseByPassword(connectionProvider);
        // doGetConnectionByToken(connectionProvider);
        // doGetConnectionByPassword(connectionProvider);
        // doCreateConfig(connectionProvider);

    }

    private void doGetConnection(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnection();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void doRefreshConnection(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.refreshConnection();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void doGetTokenResponseByToken(final ConnectionProvider connectionProvider) {
        try {
            TokenResponse response = connectionProvider.getTokenResponseByToken(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, REFRESH_INTERVAL);
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    private void doGetTokenResponseByPassword(final ConnectionProvider connectionProvider) {
        try {
            TokenResponse response = connectionProvider.getTokenResponseByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    private void doGetConnectionByToken(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnectionByToken(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, REFRESH_INTERVAL);
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    private void doGetConnectionByPassword(final ConnectionProvider connectionProvider) {
        try {
            connectionProvider.getConnectionByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    private void doCreateConfig(final ConnectionProvider connectionProvider) {
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
