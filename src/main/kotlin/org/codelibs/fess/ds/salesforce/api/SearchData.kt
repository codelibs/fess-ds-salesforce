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
package org.codelibs.fess.ds.salesforce.api

import com.fasterxml.jackson.databind.JsonNode
import java.util.*

data class SearchData(
        val type: String,
        val id: String,
        val title: String,
        val content: String,
        val digest: String,
        val created: Date,
        val lastModified: Date,
        val thumbnail: String?
) {
    companion object {
        fun fromJson(type: String, node: JsonNode, obj: SearchLayout): SearchData = SearchData(
                type,
                node[obj.id].asText(),
                if (!node[obj.title].isNull) node[obj.title].asText() else node[obj.id].asText(),
                obj.contents.filter { !node[it].isNull }.joinToString("\n") { node[it].asText() },
                obj.digests.filter { !node[it].isNull }.joinToString("\n") { node[it].asText() },
                Date(node[obj.created].asLong()),
                Date(node[obj.lastModified].asLong()),
                obj.thumbnail?.let { if (!node[it].isNull) node[it].asText() else null }
        )
    }
}
