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
package org.codelibs.fess.ds.salesforce.util;

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

    // Note: Tests requiring valid cryptographic keys are integration tests
    // and should be run with actual credentials in an integration test environment

    public void test_getPrivateKey_withInvalidPem() {
        // Arrange
        String invalidPem = "invalid_key_format";

        // Act & Assert
        try {
            AuthUtil.getPrivateKey(invalidPem);
            fail("Should throw exception for invalid PEM");
        } catch (final Exception e) {
            // Expected
            assertNotNull(e);
        }
    }

    public void test_createJWT_withInvalidPrivateKey() {
        // Arrange
        String username = "test@example.com";
        String clientId = "test_client_id";
        String invalidPrivateKey = "invalid_key";
        String baseUrl = "https://login.salesforce.com";
        long refreshInterval = 3600;

        // Act & Assert
        try {
            AuthUtil.createJWT(username, clientId, invalidPrivateKey, baseUrl, refreshInterval);
            fail("Should throw exception for invalid private key");
        } catch (final Exception e) {
            // Expected
            assertNotNull(e);
        }
    }

    public void test_utilityClass_constructor() {
        // Verify that the utility class constructor throws IllegalStateException
        try {
            // Use reflection to test private constructor
            java.lang.reflect.Constructor<AuthUtil> constructor = AuthUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            fail("Should throw IllegalStateException");
        } catch (Exception e) {
            // Expected - the cause should be IllegalStateException
            Throwable cause = e.getCause();
            if (cause != null) {
                assertTrue(cause instanceof IllegalStateException);
                assertEquals("Utility class.", cause.getMessage());
            }
        }
    }

}
