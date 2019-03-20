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

/** 注文 */
data class SOrder(
        /** 注文 ID */
        override val id: String,
        /** 注文開始日 */
        val effectiveDate: Date,
        /** 注文終了日 */
        val endDate: Date?,
        /** 状況 */
        val status: String,
        /** 説明 */
        val description: String?,
        /** 顧客 承認日 */
        val customerAuthorizedDate: Date?,
        /** 自社 承認日 */
        val companyAuthorizedDate: Date?,
        /** 注文種別 */
        val type: String?,
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
        /** 町名・番地(納入先) */
        val shippingStreet: String?,
        /** 市区郡(納入先) */
        val shippingCity: String?,
        /** 都道府県(納入先) */
        val shippingState: String?,
        /** 郵便番号(納入先) */
        val shippingPostalCode: String?,
        /** 国(納入先) */
        val shippingCountry: String?,
        /** Shipping Geocode Accuracy */
        val shippingGeocodeAccuracy: String?,
        /** 注文名 */
        val name: String?,
        /** PO 日付 */
        val poDate: Date?,
        /** PO 番号 */
        val poNumber: String?,
        /** 注文参照番号 */
        val orderReferenceNumber: String?,
        /** 有効化日 */
        val activatedDate: Date?,
        /** 状況のカテゴリ */
        val statusCode: String,
        /** 注文番号 */
        val orderNumber: String,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?
) : SObject {
    override fun title(): String = "${super.title()}"
    override fun content(): String = listOfNotNull("").joinToString("\n")
    override val objectType: SObjects = SObjects.Order
}
