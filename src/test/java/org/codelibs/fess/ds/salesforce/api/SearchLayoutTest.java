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

import org.junit.jupiter.api.TestInfo;

import java.util.Arrays;
import java.util.List;

import org.codelibs.fess.ds.salesforce.UnitDsTestCase;

public class SearchLayoutTest extends UnitDsTestCase {

    @Override
    protected String prepareConfigFile() {
        return "test_app.xml";
    }

    @Override
    protected boolean isSuppressTestCaseTransaction() {
        return true;
    }

    public void test_constructor_withAllParameters() {
        // Arrange
        String title = "Name";
        List<String> contents = Arrays.asList("Description", "Notes");
        List<String> descriptions = Arrays.asList("Summary");
        String thumbnail = "ImageUrl";

        // Act
        SearchLayout layout = new SearchLayout(title, contents, descriptions, thumbnail);

        // Assert
        assertEquals(title, layout.getTitle());
        assertEquals(contents, layout.getContents());
        assertEquals(descriptions, layout.getDescriptions());
        assertEquals(thumbnail, layout.getThumbnail());
    }

    public void test_constructor_withNullDescriptions() {
        // Arrange
        String title = "Name";
        List<String> contents = Arrays.asList("Description", "Notes");
        String thumbnail = "ImageUrl";

        // Act
        SearchLayout layout = new SearchLayout(title, contents, null, thumbnail);

        // Assert
        assertEquals(title, layout.getTitle());
        assertEquals(contents, layout.getContents());
        // When descriptions is null, it should return contents
        assertEquals(contents, layout.getDescriptions());
        assertEquals(thumbnail, layout.getThumbnail());
    }

    public void test_getStandardFields() {
        // Arrange
        SearchLayout layout = new SearchLayout();

        // Act & Assert
        assertEquals("Id", layout.getId());
        assertEquals("CreatedDate", layout.getCreated());
        assertEquals("LastModifiedDate", layout.getLastModified());
    }

    public void test_fields_withAllFields() {
        // Arrange
        List<String> contents = Arrays.asList("Description", "Notes");
        List<String> descriptions = Arrays.asList("Summary", "Details");
        String thumbnail = "ImageUrl";
        SearchLayout layout = new SearchLayout("Name", contents, descriptions, thumbnail);

        // Act
        List<String> fields = layout.fields();

        // Assert
        assertTrue(fields.contains("Description"));
        assertTrue(fields.contains("Notes"));
        assertTrue(fields.contains("Summary"));
        assertTrue(fields.contains("Details"));
        assertTrue(fields.contains("ImageUrl"));
        assertTrue(fields.contains("Id"));
        assertTrue(fields.contains("CreatedDate"));
        assertTrue(fields.contains("LastModifiedDate"));
    }

    public void test_fields_withDuplicates() {
        // Arrange
        List<String> contents = Arrays.asList("Description", "Notes");
        List<String> descriptions = Arrays.asList("Description"); // Duplicate
        SearchLayout layout = new SearchLayout("Name", contents, descriptions, null);

        // Act
        List<String> fields = layout.fields();

        // Assert
        // Should contain only distinct values
        long descriptionCount = fields.stream().filter(f -> f.equals("Description")).count();
        assertEquals(1, descriptionCount);
    }

    public void test_fields_withNullValues() {
        // Arrange
        SearchLayout layout = new SearchLayout("Name", null, null, null);

        // Act
        List<String> fields = layout.fields();

        // Assert
        // Should only contain standard fields
        assertEquals(3, fields.size());
        assertTrue(fields.contains("Id"));
        assertTrue(fields.contains("CreatedDate"));
        assertTrue(fields.contains("LastModifiedDate"));
    }

    public void test_fields_withEmptyLists() {
        // Arrange
        SearchLayout layout = new SearchLayout("Name", Arrays.asList(), Arrays.asList(), "");

        // Act
        List<String> fields = layout.fields();

        // Assert
        // Should contain standard fields
        assertTrue(fields.contains("Id"));
        assertTrue(fields.contains("CreatedDate"));
        assertTrue(fields.contains("LastModifiedDate"));
    }
}
