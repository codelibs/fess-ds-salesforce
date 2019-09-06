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
package org.codelibs.fess.ds.salesforce.util;

import static org.codelibs.fess.ds.salesforce.SalesforceClientTest.*;

import org.dbflute.utflute.lastaflute.LastaFluteTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;

public class AuthUtilTest extends LastaFluteTestCase {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtilTest.class);

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }

    public void test_getPrivateKey() {
        try {
            PrivateKey privateKey = AuthUtil.getPrivateKey(PRIVATE_KEY);
            assertNotNull(privateKey);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }
    public void test_createJWT() {
        try {
            String jwt = AuthUtil.createJWT(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL, REFRESH_INTERVAL);
            assertNotNull(jwt);
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

}
