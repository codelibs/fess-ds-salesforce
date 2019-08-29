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
package org.codelibs.fess.ds.salesforce.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SearchLayout {
    protected String title;
    protected List<String> contents;
    protected List<String> digests;
    protected String thumbnail;
    protected static final String id = "Id";
    protected static final String created = "CreatedDate";
    protected static final String lastModified = "LastModifiedDate";

    public SearchLayout() {
    }

    public SearchLayout(final String title, final List<String> contents, final List<String> digests, final String thumbnail) {
        this.title = title;
        this.contents = contents;
        this.digests = digests;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getContents() {
        return contents;
    }

    public List<String> getDigests() {
        return digests != null ? digests : getContents();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getLastModified() {
        return lastModified;
    }

    public List<String> fields() {
        final List<String> fields = new ArrayList<>();
        if(contents != null) {
            fields.addAll(contents);
        }
        if(digests != null) {
            fields.addAll(digests);
        }
        if(thumbnail != null) {
            fields.add(thumbnail);
        }
        fields.add(id);
        fields.add(created);
        fields.add(lastModified);
        return new ArrayList<>(new HashSet<>(fields));
    }

}
