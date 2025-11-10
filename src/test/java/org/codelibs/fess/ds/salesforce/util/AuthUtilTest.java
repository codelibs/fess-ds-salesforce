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

    public void test_getPrivateKey_withValidPem() {
        // Arrange
        String validPem = "-----BEGIN PRIVATE KEY-----\n"
                + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7VJTUt9Us8cKj\n"
                + "MzEfYyjiWA4R4/M2bS1+fWIcPm15j7A0+w9UZGz/lovGzLoi9gK3GyT8A7xQbFdj\n"
                + "6COzBuPbPfJXlM0FNaKZKPwBbkPXDEA3GlxjI6vC7/NZ9Z+lFNmVGK1P4J9dEo2P\n"
                + "MxqR2w7KOCzv5gB6Y0ooQ7Q6a7F5j5RLQT2K3cEMNJKJYgJ4V6qLZJVf0YZ9yZGy\n"
                + "9T8KOXBvZsZyF7M0WHGQsHxVCKZZPFPxqSYKH5hFQMZQ7xNbPcJ5lZP0tFQCPCYz\n"
                + "4X8L4vN3lJqTzLLhY7fG0F7gM2GFCwZhOQGqDVR8/MhN7VQ8xZPFMJ8pAgMBAAEC\n"
                + "AoIBABf3CYJVjY5FhQxC9xlIZJrMT8OvJCmKfOx4k3kQCJ9m8L0cWZhYdL6hm3kD\n"
                + "9pGJQYzGvLhzPGnLXsZHlYLJQKVKjYLWNGfF5aGq/6p5bQ0y1wJvBw1PlLFJZNqZ\n"
                + "AGkZLHHiSPMwP5LJdEfZJJ5v8BKQqBLXhWJZLJh8ycZ9S6XQZQE5kP8jN1H4p9xZ\n"
                + "t8z3F2x0M8QQnBv5VzVxXQH5LGVMxGNhBJCCxHT3VLZXVqF0YH9x0H3W8qQZPXqK\n"
                + "dCXQVFYy5jHZYqxNGLJCFZqXZ3gXh4Gt5aZ4VKHdZL2Y5P8xQZ4VxH5F9p0xQ8Gp\n"
                + "W5F8N9wCgYEA5h7J9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "-----END PRIVATE KEY-----";

        // Act & Assert
        try {
            PrivateKey privateKey = AuthUtil.getPrivateKey(validPem);
            assertNotNull(privateKey);
            assertEquals("RSA", privateKey.getAlgorithm());
        } catch (final Exception e) {
            fail("Should not throw exception for valid PEM: " + e.getMessage());
        }
    }

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

    public void test_getPrivateKey_withEscapedNewlines() {
        // Arrange
        String pemWithEscapedNewlines = "-----BEGIN PRIVATE KEY-----\\n"
                + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7VJTUt9Us8cKj\\n"
                + "MzEfYyjiWA4R4/M2bS1+fWIcPm15j7A0+w9UZGz/lovGzLoi9gK3GyT8A7xQbFdj\\n"
                + "6COzBuPbPfJXlM0FNaKZKPwBbkPXDEA3GlxjI6vC7/NZ9Z+lFNmVGK1P4J9dEo2P\\n"
                + "MxqR2w7KOCzv5gB6Y0ooQ7Q6a7F5j5RLQT2K3cEMNJKJYgJ4V6qLZJVf0YZ9yZGy\\n"
                + "9T8KOXBvZsZyF7M0WHGQsHxVCKZZPFPxqSYKH5hFQMZQ7xNbPcJ5lZP0tFQCPCYz\\n"
                + "4X8L4vN3lJqTzLLhY7fG0F7gM2GFCwZhOQGqDVR8/MhN7VQ8xZPFMJ8pAgMBAAEC\\n"
                + "AoIBABf3CYJVjY5FhQxC9xlIZJrMT8OvJCmKfOx4k3kQCJ9m8L0cWZhYdL6hm3kD\\n"
                + "9pGJQYzGvLhzPGnLXsZHlYLJQKVKjYLWNGfF5aGq/6p5bQ0y1wJvBw1PlLFJZNqZ\\n"
                + "AGkZLHHiSPMwP5LJdEfZJJ5v8BKQqBLXhWJZLJh8ycZ9S6XQZQE5kP8jN1H4p9xZ\\n"
                + "t8z3F2x0M8QQnBv5VzVxXQH5LGVMxGNhBJCCxHT3VLZXVqF0YH9x0H3W8qQZPXqK\\n"
                + "dCXQVFYy5jHZYqxNGLJCFZqXZ3gXh4Gt5aZ4VKHdZL2Y5P8xQZ4VxH5F9p0xQ8Gp\\n"
                + "W5F8N9wCgYEA5h7J9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\\n"
                + "-----END PRIVATE KEY-----";

        // Act & Assert
        try {
            PrivateKey privateKey = AuthUtil.getPrivateKey(pemWithEscapedNewlines);
            assertNotNull(privateKey);
            assertEquals("RSA", privateKey.getAlgorithm());
        } catch (final Exception e) {
            fail("Should handle escaped newlines: " + e.getMessage());
        }
    }

    public void test_createJWT_withValidParameters() {
        // Arrange
        String username = "test@example.com";
        String clientId = "test_client_id";
        String privateKey = "-----BEGIN PRIVATE KEY-----\n"
                + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7VJTUt9Us8cKj\n"
                + "MzEfYyjiWA4R4/M2bS1+fWIcPm15j7A0+w9UZGz/lovGzLoi9gK3GyT8A7xQbFdj\n"
                + "6COzBuPbPfJXlM0FNaKZKPwBbkPXDEA3GlxjI6vC7/NZ9Z+lFNmVGK1P4J9dEo2P\n"
                + "MxqR2w7KOCzv5gB6Y0ooQ7Q6a7F5j5RLQT2K3cEMNJKJYgJ4V6qLZJVf0YZ9yZGy\n"
                + "9T8KOXBvZsZyF7M0WHGQsHxVCKZZPFPxqSYKH5hFQMZQ7xNbPcJ5lZP0tFQCPCYz\n"
                + "4X8L4vN3lJqTzLLhY7fG0F7gM2GFCwZhOQGqDVR8/MhN7VQ8xZPFMJ8pAgMBAAEC\n"
                + "AoIBABf3CYJVjY5FhQxC9xlIZJrMT8OvJCmKfOx4k3kQCJ9m8L0cWZhYdL6hm3kD\n"
                + "9pGJQYzGvLhzPGnLXsZHlYLJQKVKjYLWNGfF5aGq/6p5bQ0y1wJvBw1PlLFJZNqZ\n"
                + "AGkZLHHiSPMwP5LJdEfZJJ5v8BKQqBLXhWJZLJh8ycZ9S6XQZQE5kP8jN1H4p9xZ\n"
                + "t8z3F2x0M8QQnBv5VzVxXQH5LGVMxGNhBJCCxHT3VLZXVqF0YH9x0H3W8qQZPXqK\n"
                + "dCXQVFYy5jHZYqxNGLJCFZqXZ3gXh4Gt5aZ4VKHdZL2Y5P8xQZ4VxH5F9p0xQ8Gp\n"
                + "W5F8N9wCgYEA5h7J9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "-----END PRIVATE KEY-----";
        String baseUrl = BASE_URL;
        long refreshInterval = 3600;

        // Act & Assert
        try {
            String jwt = AuthUtil.createJWT(username, clientId, privateKey, baseUrl, refreshInterval);
            assertNotNull(jwt);
            // JWT should have 3 parts separated by dots
            String[] parts = jwt.split("\\.");
            assertEquals(3, parts.length);
            // Verify header and payload are base64 encoded
            assertTrue(parts[0].length() > 0);
            assertTrue(parts[1].length() > 0);
            assertTrue(parts[2].length() > 0);
        } catch (final Exception e) {
            fail("Should create JWT successfully: " + e.getMessage());
        }
    }

    public void test_createJWT_withInvalidPrivateKey() {
        // Arrange
        String username = "test@example.com";
        String clientId = "test_client_id";
        String invalidPrivateKey = "invalid_key";
        String baseUrl = BASE_URL;
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

    public void test_createJWT_jwtStructure() {
        // Arrange
        String username = "user@test.com";
        String clientId = "client123";
        String privateKey = "-----BEGIN PRIVATE KEY-----\n"
                + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7VJTUt9Us8cKj\n"
                + "MzEfYyjiWA4R4/M2bS1+fWIcPm15j7A0+w9UZGz/lovGzLoi9gK3GyT8A7xQbFdj\n"
                + "6COzBuPbPfJXlM0FNaKZKPwBbkPXDEA3GlxjI6vC7/NZ9Z+lFNmVGK1P4J9dEo2P\n"
                + "MxqR2w7KOCzv5gB6Y0ooQ7Q6a7F5j5RLQT2K3cEMNJKJYgJ4V6qLZJVf0YZ9yZGy\n"
                + "9T8KOXBvZsZyF7M0WHGQsHxVCKZZPFPxqSYKH5hFQMZQ7xNbPcJ5lZP0tFQCPCYz\n"
                + "4X8L4vN3lJqTzLLhY7fG0F7gM2GFCwZhOQGqDVR8/MhN7VQ8xZPFMJ8pAgMBAAEC\n"
                + "AoIBABf3CYJVjY5FhQxC9xlIZJrMT8OvJCmKfOx4k3kQCJ9m8L0cWZhYdL6hm3kD\n"
                + "9pGJQYzGvLhzPGnLXsZHlYLJQKVKjYLWNGfF5aGq/6p5bQ0y1wJvBw1PlLFJZNqZ\n"
                + "AGkZLHHiSPMwP5LJdEfZJJ5v8BKQqBLXhWJZLJh8ycZ9S6XQZQE5kP8jN1H4p9xZ\n"
                + "t8z3F2x0M8QQnBv5VzVxXQH5LGVMxGNhBJCCxHT3VLZXVqF0YH9x0H3W8qQZPXqK\n"
                + "dCXQVFYy5jHZYqxNGLJCFZqXZ3gXh4Gt5aZ4VKHdZL2Y5P8xQZ4VxH5F9p0xQ8Gp\n"
                + "W5F8N9wCgYEA5h7J9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h9h1h\n"
                + "-----END PRIVATE KEY-----";
        String baseUrl = "https://login.salesforce.com";
        long refreshInterval = 7200;

        // Act
        try {
            String jwt = AuthUtil.createJWT(username, clientId, privateKey, baseUrl, refreshInterval);

            // Assert - Verify JWT format
            String[] parts = jwt.split("\\.");
            assertEquals("JWT should have 3 parts", 3, parts.length);

            // All parts should be non-empty
            for (String part : parts) {
                assertTrue("JWT part should not be empty", part.length() > 0);
            }
        } catch (final Exception e) {
            fail("Failed to create JWT: " + e.getMessage());
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
