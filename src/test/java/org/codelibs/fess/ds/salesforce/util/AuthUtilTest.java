/*
 * Copyright 2012-2024 CodeLibs Project and the Others.
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
package org.codelibs.fess.ds.salesforce.util;

import static org.codelibs.fess.ds.salesforce.SalesforceClientTest.BASE_URL;
import static org.codelibs.fess.ds.salesforce.SalesforceClientTest.CLIENT_ID;
import static org.codelibs.fess.ds.salesforce.SalesforceClientTest.PRIVATE_KEY;
import static org.codelibs.fess.ds.salesforce.SalesforceClientTest.REFRESH_INTERVAL;
import static org.codelibs.fess.ds.salesforce.SalesforceClientTest.USERNAME;

import java.security.PrivateKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbflute.utflute.lastaflute.LastaFluteTestCase;

public class AuthUtilTest extends LastaFluteTestCase {

    private static final Logger logger = LogManager.getLogger(AuthUtilTest.class);

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }

    public void test() {
        // doGetPrivateKey();
        // doCreateJWT();
    }

    protected void doGetPrivateKey() {
        try {
            PrivateKey privateKey = AuthUtil.getPrivateKey(PRIVATE_KEY);
            assertNotNull(privateKey);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    protected void doCreateJWT() {
        try {
            String jwt = AuthUtil.createJWT(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, REFRESH_INTERVAL);
            assertNotNull(jwt);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
