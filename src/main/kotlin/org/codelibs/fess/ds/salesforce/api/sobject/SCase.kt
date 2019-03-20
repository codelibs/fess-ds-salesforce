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

/** ケース */
data class SCase(
        /** ケース ID */
        override val id: String,
        /** ケース番号 */
        val caseNumber: String,
        /** 名前 */
        val suppliedName: String?,
        /** メールアドレス */
        val suppliedEmail: String?,
        /** 電話 */
        val suppliedPhone: String?,
        /** 会社名 */
        val suppliedCompany: String?,
        /** ケース種別 */
        val type: String?,
        /** 状況 */
        val status: String?,
        /** 原因 */
        val reason: String?,
        /** 発生源 */
        val origin: String?,
        /** 件名 */
        val subject: String?,
        /** 優先度 */
        val priority: String?,
        /** 説明 */
        val description: String?,
        /** 完了日 */
        val closedDate: Date?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 取引先責任者 電話 */
        val contactPhone: String?,
        /** 取引先責任者 携帯 */
        val contactMobile: String?,
        /** 取引先責任者 メール */
        val contactEmail: String?,
        /** 取引先責任者 Fax */
        val contactFax: String?,
        /** 社内コメント */
        val comments: String?,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?
) : SObject {
    override fun title(): String = "${super.title()}"
    override fun content(): String = listOfNotNull("").joinToString("\n")
    override val objectType: SObjects = SObjects.Case
}
