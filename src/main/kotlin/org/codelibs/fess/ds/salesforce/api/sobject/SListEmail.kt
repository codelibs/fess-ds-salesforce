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

/** リストメール */
data class SListEmail(
        /** リストメール ID */
        override val id: String,
        /** 名前 */
        val name: String,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?,
        /** 件名 */
        val subject: String?,
        /** HTML 内容 */
        val htmlBody: String?,
        /** テキスト内容 */
        val textBody: String?,
        /** 差出人名 */
        val fromName: String?,
        /** 送信元アドレス */
        val fromAddress: String,
        /** 状況 */
        val status: String,
        /** 予定日 */
        val scheduledDate: Date?
) : SObject {
    override fun title(): String = "${super.title()} $name"
    override fun content(): String = listOfNotNull(id, name, subject, textBody, fromName, fromAddress, status).joinToString("\n")
    override val objectType: SObjects = SObjects.ListEmail
}
