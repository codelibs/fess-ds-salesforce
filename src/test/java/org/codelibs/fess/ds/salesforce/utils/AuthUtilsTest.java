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
package org.codelibs.fess.ds.salesforce.utils;

import com.sforce.ws.ConnectionException;
import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException;
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.dbflute.utflute.lastaflute.LastaFluteTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUtilsTest extends LastaFluteTestCase {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtilsTest.class);

    public static final String BASE_URL = "";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";
    public static final String SECURITY_TOKEN = "";
    public static final String CLIENT_ID = "";
    public static final String CLIENT_SECRET = "";
    public static final String PRIVATE_KEY = "";

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }


    public void testGetTokenResponse() {
        // doGetTokenResponse()
        // doGetTokenResponseByPassword()
    }

    private void doGetTokenResponse() {
        try {
            TokenResponse response = AuthUtils.getTokenResponse(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, 3600);
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    private void doGetTokenResponseByPassword() {
        try {
            TokenResponse response = AuthUtils.getTokenResponseByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
            logger.debug("AccessToken: " + response.getAccessToken());
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    public void testGetConnection() {
        // TODO
        // doGetConnection();
        // doGetConnectionByPassword();
    }

    private void doGetConnection() {
        try {
            AuthUtils.getConnectionByOAuth(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, 3600);
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    private void doGetConnectionByPassword() {
        try {
            AuthUtils.getConnectionByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
        } catch (ConnectionException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

}