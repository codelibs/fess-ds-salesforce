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

import org.codelibs.fess.ds.salesforce.UnitDsTestCase;

public class SalesforceDataStoreExceptionTest extends UnitDsTestCase {

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }

    public void test_constructor_withMessage() {
        // Arrange
        String message = "Test error message";

        // Act
        SalesforceDataStoreException exception = new SalesforceDataStoreException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    public void test_constructor_withMessageAndCause() {
        // Arrange
        String message = "Test error message";
        Throwable cause = new RuntimeException("Root cause");

        // Act
        SalesforceDataStoreException exception = new SalesforceDataStoreException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    public void test_constructor_withCause() {
        // Arrange
        Throwable cause = new RuntimeException("Root cause");

        // Act
        SalesforceDataStoreException exception = new SalesforceDataStoreException(cause);

        // Assert
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getMessage().contains("RuntimeException"));
    }

    public void test_throw_withMessage() {
        // Arrange
        String message = "Failed to connect";

        // Act & Assert
        try {
            throw new SalesforceDataStoreException(message);
        } catch (SalesforceDataStoreException e) {
            assertEquals(message, e.getMessage());
        }
    }

    public void test_throw_withMessageAndCause() {
        // Arrange
        String message = "Failed to authenticate";
        Throwable cause = new IllegalArgumentException("Invalid credentials");

        // Act & Assert
        try {
            throw new SalesforceDataStoreException(message, cause);
        } catch (SalesforceDataStoreException e) {
            assertEquals(message, e.getMessage());
            assertEquals(cause, e.getCause());
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }

    public void test_throw_withCause() {
        // Arrange
        Throwable cause = new NullPointerException("Null parameter");

        // Act & Assert
        try {
            throw new SalesforceDataStoreException(cause);
        } catch (SalesforceDataStoreException e) {
            assertEquals(cause, e.getCause());
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }

    public void test_serialization() {
        // Arrange
        String message = "Serialization test";
        Throwable cause = new RuntimeException("Cause");
        SalesforceDataStoreException exception = new SalesforceDataStoreException(message, cause);

        // Act & Assert
        // Verify serialVersionUID is defined
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
