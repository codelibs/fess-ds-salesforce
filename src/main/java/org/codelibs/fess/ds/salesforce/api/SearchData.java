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

public class SearchData {
    protected String type;
    protected String id;
    protected String title;
    protected String content;
    protected String description;
    protected Date created;
    protected Date lastModified;
    protected String thumbnail;

    public SearchData(final String type, final JsonNode node, final SearchLayout obj) {
        this.type = type;
        this.id = node.get(obj.getId()).asText();
        if (node.get(obj.getTitle()) != null) {
            title = node.get(obj.getTitle()).asText();
        } else {
            title = StringUtil.EMPTY;
        }
        if (obj.getContents() != null) {
            this.content = obj.getContents().stream().filter(o -> !node.get(o).isNull()).map(o -> node.get(o).asText())
                    .collect(Collectors.joining("\n"));
        } else {
            this.content = StringUtil.EMPTY;
        }
        if (obj.getDescriptions() != null) {
            this.description = obj.getDescriptions().stream().filter(o -> !node.get(o).isNull()).map(o -> node.get(o).asText())
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

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreated() {
        return created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
