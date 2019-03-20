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

/** 取引先責任者 */
data class SContact(
        /** 取引先責任者 ID */
        override val id: String,
        /** 姓 */
        val lastName: String,
        /** 名 */
        val firstName: String?,
        /** 敬称 */
        val salutation: String?,
        /** 氏名 */
        val name: String,
        /** 町名・番地(その他) */
        val otherStreet: String?,
        /** 市区郡(その他) */
        val otherCity: String?,
        /** 都道府県(その他) */
        val otherState: String?,
        /** 郵便番号(その他) */
        val otherPostalCode: String?,
        /** 国(その他) */
        val otherCountry: String?,
        /** Other Geocode Accuracy */
        val otherGeocodeAccuracy: String?,
        /** 町名・番地(郵送先) */
        val mailingStreet: String?,
        /** 市区郡(郵送先) */
        val mailingCity: String?,
        /** 都道府県(郵送先) */
        val mailingState: String?,
        /** 郵便番号(郵送先) */
        val mailingPostalCode: String?,
        /** 国(郵送先) */
        val mailingCountry: String?,
        /** Mailing Geocode Accuracy */
        val mailingGeocodeAccuracy: String?,
        /** 電話 */
        val phone: String?,
        /** Fax */
        val fax: String?,
        /** 携帯電話 */
        val mobilePhone: String?,
        /** 自宅電話 */
        val homePhone: String?,
        /** その他の電話 */
        val otherPhone: String?,
        /** アシスタント電話 */
        val assistantPhone: String?,
        /** メール */
        val email: String?,
        /** 役職 */
        val title: String?,
        /** 部署 */
        val department: String?,
        /** アシスタント名 */
        val assistantName: String?,
        /** リードソース */
        val leadSource: String?,
        /** 誕生日 */
        val birthdate: Date?,
        /** 取引先責任者 説明 */
        val description: String?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 最終活動日 */
        val lastActivityDate: Date?,
        /** 登録情報照会 最終依頼日 */
        val lastCURequestDate: Date?,
        /** 登録情報照会 最終更新日 */
        val lastCUUpdateDate: Date?,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?,
        /** メール不達の理由 */
        val emailBouncedReason: String?,
        /** メール不達発生日 */
        val emailBouncedDate: Date?,
        /** 写真の URL */
        val photoUrl: String?,
        /** Data.com キー */
        val jigsaw: String?,
        /** Jigsaw Contact ID */
        val jigsawContactId: String?,
        /** 状況をクリーンアップ */
        val cleanStatus: String?
) : SObject {
    override fun title(): String = "${super.title()} $name"
    override fun content(): String = listOfNotNull(id, name, phone, fax, mobilePhone, homePhone, otherPhone, email, title, department, description).joinToString("\n")
    override fun thumbnail(): String? = photoUrl
    override val objectType: SObjects = SObjects.Contact
}
