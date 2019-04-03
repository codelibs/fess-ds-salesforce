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

interface SearchLayout {
    val title: String
    val contents: List<String>
    val digests: List<String> get() = contents
    val thumbnail: String? get() = null

    val id: String get() = "Id"
    val created: String get() = "CreatedDate"
    val lastModified: String get() = "LastModifiedDate"

    fun fields(): List<String> = (contents + digests + listOfNotNull(id, created, lastModified, thumbnail)).distinct()
}
