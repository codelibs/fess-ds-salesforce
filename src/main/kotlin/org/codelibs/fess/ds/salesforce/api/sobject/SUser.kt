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
package org.codelibs.fess.ds.salesforce.api.sobject

import java.util.*

data class SUser(
        override val Id: String,
        val Username: String,
        val Name: String,
        val CompanyName: String?,
        val Division: String?,
        val Department: String?,
        val Title: String?,
        val Email: String,
        val Phone: String?,
        val Fax: String?,
        val CreatedDate: Date,
        val LastModifiedDate: Date,
        val AboutMe: String?,
        val SmallPhotoUrl: String?
) : Searchable {
    override fun title(): String = "$Name ($Username)"
    override fun content(): String = listOfNotNull(CompanyName, Division, Department, Title, Email, Phone, Fax, AboutMe).joinToString("\n")
    override fun created(): Date = CreatedDate
    override fun lastModified(): Date = LastModifiedDate
    override fun thumbnail(): String? = SmallPhotoUrl
}