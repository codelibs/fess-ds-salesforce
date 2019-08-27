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
package org.codelibs.fess.ds.salesforce.api;

import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException;
import org.dbflute.utflute.lastaflute.LastaFluteTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.codelibs.fess.ds.salesforce.api.Authentications.*;

public class AuthenticationTest extends LastaFluteTestCase {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationTest.class);

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
            TokenResponse response = getTokenResponse(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL);
            logger.debug("AccessToken: " + response.accessToken);
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    private void doGetTokenResponseByPassword() {
        try {
            TokenResponse response = getTokenResponseByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
            logger.debug("AccessToken: ${response.accessToken}");
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get AccessToken by '" + e.getMessage() + "'");
        }
    }

    public void testGetConnection() {
        // doGetConnection()
        // doGetConnectionByPassword()
    }

    private void doGetConnection() {
        try {
            getConnection(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL);
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

    private void doGetConnectionByPassword() {
        try {
            getConnectionByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL);
        } catch (SalesforceDataStoreException e) {
            fail("Failed to get connection by '" + e.getMessage() + "'");
        }
    }

}