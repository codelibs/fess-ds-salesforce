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

/** 取引先 */
data class SAccount(
        /** 取引先 ID */
        override val id: String,
        /** 取引先名 */
        val name: String,
        /** 取引先 種別 */
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
        /** 取引先 電話 */
        val phone: String?,
        /** 取引先 Fax */
        val fax: String?,
        /** 取引先番号 */
        val accountNumber: String?,
        /** Web サイト */
        val website: String?,
        /** 写真の URL */
        val photoUrl: String?,
        /** 産業コード */
        val sic: String?,
        /** 業種 */
        val industry: String?,
        /** 会社形態 */
        val ownership: String?,
        /** 株式コード */
        val tickerSymbol: String?,
        /** 取引先 説明 */
        val description: String?,
        /** 取引先 評価 */
        val rating: String?,
        /** 取引先 部門 */
        val site: String?,
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
        /** Jigsaw Company ID */
        val jigsawCompanyId: String?,
        /** 状況をクリーンアップ */
        val cleanStatus: String?,
        /** 取引先ソース */
        val accountSource: String?,
        /** D-U-N-S 番号 */
        val dunsNumber: String?,
        /** 取引形態 */
        val tradestyle: String?,
        /** NAICS コード */
        val naicsCode: String?,
        /** NAICS の説明 */
        val naicsDesc: String?,
        /** 開始年 */
        val yearStarted: String?,
        /** 産業区分の説明 */
        val sicDesc: String?
) : SObject {
    override fun title(): String = "${super.title()}"
    override fun content(): String = listOfNotNull("").joinToString("\n")
    override val objectType: SObjects = SObjects.Account
}
