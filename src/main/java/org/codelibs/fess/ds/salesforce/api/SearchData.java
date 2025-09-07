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

import java.util.Date;
import java.util.stream.Collectors;

import org.codelibs.core.lang.StringUtil;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents a search data object retrieved from Salesforce.
 */
public class SearchData {
    /** The type of the SObject. */
    protected String type;
    /** The ID of the SObject. */
    protected String id;
    /** The title of the search data. */
    protected String title;
    /** The content of the search data. */
    protected String content;
    /** The description of the search data. */
    protected String description;
    /** The creation date of the SObject. */
    protected Date created;
    /** The last modified date of the SObject. */
    protected Date lastModified;
    /** The thumbnail URL of the search data. */
    protected String thumbnail;

    /**
     * Constructs a new SearchData object.
     *
     * @param type The type of the SObject.
     * @param node The JSON node representing the SObject.
     * @param obj The search layout.
     */
    public SearchData(final String type, final JsonNode node, final SearchLayout obj) {
        this.type = type;
        this.id = node.get(obj.getId()).asText();
        if (node.get(obj.getTitle()) != null) {
            title = node.get(obj.getTitle()).asText();
        } else {
            title = StringUtil.EMPTY;
        }
        if (obj.getContents() != null) {
            this.content = obj.getContents()
                    .stream()
                    .filter(o -> !node.get(o).isNull())
                    .map(o -> node.get(o).asText())
                    .collect(Collectors.joining("\n"));
        } else {
            this.content = StringUtil.EMPTY;
        }
        if (obj.getDescriptions() != null) {
            this.description = obj.getDescriptions()
                    .stream()
                    .filter(o -> !node.get(o).isNull())
                    .map(o -> node.get(o).asText())
                    .collect(Collectors.joining("\n"));
        } else {
            this.description = StringUtil.EMPTY;
        }
        this.created = new Date(node.get(obj.getCreated()).asLong());
        this.lastModified = new Date(node.get(obj.getLastModified()).asLong());
        if (obj.getThumbnail() != null && node.get(obj.getThumbnail()) != null) {
            this.thumbnail = node.get(obj.getThumbnail()).asText();
        } else {
            this.thumbnail = StringUtil.EMPTY;
        }
    }

    /**
     * Returns the type of the SObject.
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the ID of the SObject.
     *
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the title of the search data.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the content of the search data.
     *
     * @return The content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the description of the search data.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the creation date of the SObject.
     *
     * @return The creation date.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Returns the last modified date of the SObject.
     *
     * @return The last modified date.
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * Returns the thumbnail URL of the search data.
     *
     * @return The thumbnail URL.
     */
    public String getThumbnail() {
        return thumbnail;
    }
}