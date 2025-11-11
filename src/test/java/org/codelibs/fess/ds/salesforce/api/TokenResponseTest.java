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
package org.codelibs.fess.ds.salesforce.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.dbflute.utflute.lastaflute.LastaFluteTestCase;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenResponseTest extends LastaFluteTestCase {

    private ObjectMapper mapper;

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
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void test_deserialize_successResponse() throws IOException {
        // Arrange
        String json = "{"
                + "\"access_token\":\"test_access_token\","
                + "\"instance_url\":\"https://test.salesforce.com\","
                + "\"id\":\"https://login.salesforce.com/id/00D/005\","
                + "\"token_type\":\"Bearer\""
                + "}";

        // Act
        TokenResponse response = mapper.readValue(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                TokenResponse.class);

        // Assert
        assertEquals("test_access_token", response.getAccessToken());
        assertEquals("https://test.salesforce.com", response.getInstanceUrl());
        assertEquals("https://login.salesforce.com/id/00D/005", response.getId());
        assertEquals("Bearer", response.getTokenType());
        assertNull(response.getError());
        assertNull(response.getErrorDescription());
    }

    public void test_deserialize_errorResponse() throws IOException {
        // Arrange
        String json = "{"
                + "\"error\":\"invalid_grant\","
                + "\"error_description\":\"authentication failure\""
                + "}";

        // Act
        TokenResponse response = mapper.readValue(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                TokenResponse.class);

        // Assert
        assertNull(response.getAccessToken());
        assertNull(response.getInstanceUrl());
        assertNull(response.getId());
        assertNull(response.getTokenType());
        assertEquals("invalid_grant", response.getError());
        assertEquals("authentication failure", response.getErrorDescription());
    }

    public void test_deserialize_withUnknownProperties() throws IOException {
        // Arrange
        String json = "{"
                + "\"access_token\":\"test_token\","
                + "\"instance_url\":\"https://test.salesforce.com\","
                + "\"id\":\"test_id\","
                + "\"token_type\":\"Bearer\","
                + "\"unknown_field\":\"should_be_ignored\","
                + "\"another_unknown\":123"
                + "}";

        // Act
        TokenResponse response = mapper.readValue(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                TokenResponse.class);

        // Assert
        assertEquals("test_token", response.getAccessToken());
        assertEquals("https://test.salesforce.com", response.getInstanceUrl());
        assertEquals("test_id", response.getId());
        assertEquals("Bearer", response.getTokenType());
    }

    public void test_toString_successResponse() {
        // Arrange
        String json = "{"
                + "\"access_token\":\"test_token\","
                + "\"instance_url\":\"https://test.salesforce.com\","
                + "\"id\":\"test_id\","
                + "\"token_type\":\"Bearer\""
                + "}";

        // Act
        TokenResponse response;
        try {
            response = mapper.readValue(
                    new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                    TokenResponse.class);
        } catch (IOException e) {
            fail("Failed to parse JSON: " + e.getMessage());
            return;
        }
        String result = response.toString();

        // Assert
        assertTrue(result.contains("test_token"));
        assertTrue(result.contains("https://test.salesforce.com"));
        assertTrue(result.contains("test_id"));
        assertTrue(result.contains("Bearer"));
    }

    public void test_toString_errorResponse() {
        // Arrange
        String json = "{"
                + "\"error\":\"invalid_grant\","
                + "\"error_description\":\"authentication failure\""
                + "}";

        // Act
        TokenResponse response;
        try {
            response = mapper.readValue(
                    new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                    TokenResponse.class);
        } catch (IOException e) {
            fail("Failed to parse JSON: " + e.getMessage());
            return;
        }
        String result = response.toString();

        // Assert
        assertTrue(result.contains("invalid_grant"));
        assertTrue(result.contains("authentication failure"));
    }

    public void test_deserialize_snakeCaseConversion() throws IOException {
        // Arrange - testing snake_case to camelCase conversion
        String json = "{"
                + "\"access_token\":\"token_value\","
                + "\"instance_url\":\"url_value\","
                + "\"token_type\":\"type_value\","
                + "\"error_description\":\"desc_value\""
                + "}";

        // Act
        TokenResponse response = mapper.readValue(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                TokenResponse.class);

        // Assert - verify snake_case properties are properly mapped to camelCase
        assertEquals("token_value", response.getAccessToken());
        assertEquals("url_value", response.getInstanceUrl());
        assertEquals("type_value", response.getTokenType());
        assertEquals("desc_value", response.getErrorDescription());
    }

    public void test_deserialize_emptyJson() throws IOException {
        // Arrange
        String json = "{}";

        // Act
        TokenResponse response = mapper.readValue(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)),
                TokenResponse.class);

        // Assert
        assertNull(response.getAccessToken());
        assertNull(response.getInstanceUrl());
        assertNull(response.getId());
        assertNull(response.getTokenType());
        assertNull(response.getError());
        assertNull(response.getErrorDescription());
    }
}
