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

/** 契約 */
data class SContract(
        /** 契約 ID */
        override val id: String,
        /** 所有者に対する終了通知 */
        val ownerExpirationNotice: String?,
        /** 契約開始日 */
        val startDate: Date?,
        /** 契約終了日 */
        val endDate: Date?,
        /** 町名・番地(請求先) */
        val billingStreet: String?,
        /** 市区郡(請求先) */
        val billingCity: String?,
        /** 都道府県(請求先) */
        val billingState: String?,
        /** 郵便番号(請求先) */
        val billingPostalCode: String?,
        /** 国(請求先) */
        val billingCountry: String?,
        /** Billing Geocode Accuracy */
        val billingGeocodeAccuracy: String?,
        /** 状況 */
        val status: String,
        /** 自社 契約日 */
        val companySignedDate: Date?,
        /** 顧客 調印者役職 */
        val customerSignedTitle: String?,
        /** 顧客 契約日 */
        val customerSignedDate: Date?,
        /** 特記事項 */
        val specialTerms: String?,
        /** 有効日 */
        val activatedDate: Date?,
        /** 状況のカテゴリ */
        val statusCode: String,
        /** 説明 */
        val description: String?,
        /** 契約番号 */
        val contractNumber: String,
        /** 最終承認日 */
        val lastApprovedDate: Date?,
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
        val lastReferencedDate: Date?
) : SObject {
    override fun title(): String = "${super.title()}"
    override fun content(): String = listOfNotNull("").joinToString("\n")
    override val objectType: SObjects = SObjects.Contract
}
