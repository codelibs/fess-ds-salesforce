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

import org.junit.jupiter.api.TestInfo;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codelibs.fess.ds.salesforce.UnitDsTestCase;

public class BulkUtilTest extends UnitDsTestCase {

    private static final Logger logger = LogManager.getLogger(BulkUtil.class);

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }

    public void test_createQuery_withMultipleFields() {
        // Arrange
        String objectType = "Account";
        List<String> fields = Arrays.asList("Id", "Name", "Description");

        // Act
        String query = BulkUtil.createQuery(objectType, fields);

        // Assert
        assertEquals("SELECT Id,Name,Description FROM Account", query);
    }

    public void test_createQuery_withSingleField() {
        // Arrange
        String objectType = "Contact";
        List<String> fields = Arrays.asList("Email");

        // Act
        String query = BulkUtil.createQuery(objectType, fields);

        // Assert
        assertEquals("SELECT Email FROM Contact", query);
    }

    public void test_createQuery_withManyFields() {
        // Arrange
        String objectType = "Opportunity";
        List<String> fields = Arrays.asList("Id", "Name", "Amount", "CloseDate", "StageName", "AccountId");

        // Act
        String query = BulkUtil.createQuery(objectType, fields);

        // Assert
        assertEquals("SELECT Id,Name,Amount,CloseDate,StageName,AccountId FROM Opportunity", query);
    }

    public void test_createQuery_withEmptyFields() {
        // Arrange
        String objectType = "Lead";
        List<String> fields = Arrays.asList();

        // Act
        String query = BulkUtil.createQuery(objectType, fields);

        // Assert
        assertEquals("SELECT  FROM Lead", query);
    }

    public void test_createQuery_verifyFormat() {
        // Arrange
        String objectType = "Case";
        List<String> fields = Arrays.asList("CaseNumber", "Subject", "Status");

        // Act
        String query = BulkUtil.createQuery(objectType, fields);

        // Assert
        assertTrue(query.startsWith("SELECT "));
        assertTrue(query.contains(" FROM "));
        assertTrue(query.endsWith("Case"));
        assertTrue(query.contains("CaseNumber"));
        assertTrue(query.contains("Subject"));
        assertTrue(query.contains("Status"));
    }

    public void test_utilityClass_constructor() {
        // Verify that the utility class constructor throws IllegalStateException
        try {
            // Use reflection to test private constructor
            java.lang.reflect.Constructor<BulkUtil> constructor = BulkUtil.class.getDeclaredConstructor();
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
