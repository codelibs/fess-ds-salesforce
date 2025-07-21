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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines the layout for search result data, mapping Salesforce SObject fields
 * to Fess's standard search fields like title, content, and description.
 */
public class SearchLayout {
    private String title;
    private List<String> contents;
    private List<String> descriptions;
    private String thumbnail;
    private static final String ID = "Id";
    private static final String CREATED_DATE = "CreatedDate";
    private static final String LAST_MODIFIED_DATE = "LastModifiedDate";

    /**
     * Default constructor.
     */
    public SearchLayout() {
    }

    /**
     * Constructs a new SearchLayout.
     *
     * @param title The title field.
     * @param contents The content fields.
     * @param descriptions The description fields.
     * @param thumbnail The thumbnail field.
     */
    public SearchLayout(final String title, final List<String> contents, final List<String> descriptions, final String thumbnail) {
        this.title = title;
        this.contents = contents;
        this.descriptions = descriptions;
        this.thumbnail = thumbnail;
    }

    /**
     * Gets the field name to be used as the title of the search result.
     * @return The title field name.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the list of field names to be used as the main content of the search result.
     * @return The list of content field names.
     */
    public List<String> getContents() {
        return contents;
    }

    /**
     * Gets the list of field names to be used as the description of the search result.
     * @return The list of description field names.
     */
    public List<String> getDescriptions() {
        return descriptions != null ? descriptions : getContents();
    }

    /**
     * Gets the field name to be used as the thumbnail of the search result.
     * @return The thumbnail field name.
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Gets the ID field name.
     * @return The ID field name.
     */
    public String getId() {
        return ID;
    }

    /**
     * Gets the created date field name.
     * @return The created date field name.
     */
    public String getCreated() {
        return CREATED_DATE;
    }

    /**
     * Gets the last modified date field name.
     * @return The last modified date field name.
     */
    public String getLastModified() {
        return LAST_MODIFIED_DATE;
    }

    /**
     * Returns a list of all fields in the layout.
     * @return A list of all fields.
     */
    public List<String> fields() {
        final List<String> fields = new ArrayList<>();
        if (contents != null) {
            fields.addAll(contents);
        }
        if (descriptions != null) {
            fields.addAll(descriptions);
        }
        if (thumbnail != null) {
            fields.add(thumbnail);
        }
        fields.add(getId());
        fields.add(getCreated());
        fields.add(getLastModified());
        return fields.stream().distinct().collect(Collectors.toList());
    }

}
