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

/** リード */
data class SLead(
        /** リード ID */
        override val id: String,
        /** 姓 */
        val lastName: String,
        /** 名 */
        val firstName: String?,
        /** 敬称 */
        val salutation: String?,
        /** 氏名 */
        val name: String,
        /** 役職 */
        val title: String?,
        /** 会社名 */
        val company: String,
        /** 町名・番地 */
        val street: String?,
        /** 市区郡 */
        val city: String?,
        /** 都道府県 */
        val state: String?,
        /** 郵便番号 */
        val postalCode: String?,
        /** 国 */
        val country: String?,
        /** Geocode Accuracy */
        val geocodeAccuracy: String?,
        /** 電話 */
        val phone: String?,
        /** 携帯電話 */
        val mobilePhone: String?,
        /** Fax */
        val fax: String?,
        /** メール */
        val email: String?,
        /** Web サイト */
        val website: String?,
        /** 写真の URL */
        val photoUrl: String?,
        /** 説明 */
        val description: String?,
        /** リードソース */
        val leadSource: String?,
        /** 状況 */
        val status: String,
        /** 業種 */
        val industry: String?,
        /** 評価 */
        val rating: String?,
        /** 取引開始日 */
        val convertedDate: Date?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 最終活動日 */
        val lastActivityDate: Date?,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?,
        /** Data.com キー */
        val jigsaw: String?,
        /** Jigsaw Contact ID */
        val jigsawContactId: String?,
        /** 状況をクリーンアップ */
        val cleanStatus: String?,
        /** 会社 D-U-N-S 番号 */
        val companyDunsNumber: String?,
        /** メール不達の理由 */
        val emailBouncedReason: String?,
        /** メール不達発生日 */
        val emailBouncedDate: Date?
) : SObject {
    override fun title(): String = "${super.title()} $name"
    override fun content(): String = listOfNotNull(id, name, title, company, phone, mobilePhone, fax, email, website, description, status, industry).joinToString("\n")
    override fun thumbnail(): String? = photoUrl
    override val objectType: SObjects = SObjects.Lead
}
