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

/** D&B 企業 */
data class SDandBCompany(
        /** D&B 企業 ID */
        override val id: String,
        /** 第 1 会社名 */
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
        /** D-U-N-S number */
        val dunsNumber: String,
        /** 住所 */
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
        val geocodeAccuracyStandard: String?,
        /** 電話番号 */
        val phone: String?,
        /** Fax 番号 */
        val fax: String?,
        /** 国際ダイヤルコード */
        val countryAccessCode: String?,
        /** 所有者種別インジケータ */
        val publicIndicator: String?,
        /** 株式コード */
        val stockSymbol: String?,
        /** 証券取引所 */
        val stockExchange: String?,
        /** URL */
        val uRL: String?,
        /** 倒産インジケータ */
        val outOfBusiness: String?,
        /** FIPS MSA コード */
        val fipsMsaCode: String?,
        /** FIPS MSA コードの説明 */
        val fipsMsaDesc: String?,
        /** 第 1 取引形態 */
        val tradeStyle1: String?,
        /** 設立年 */
        val yearStarted: String?,
        /** 町名・番地(郵送先) */
        val mailingStreet: String?,
        /** 市区郡(郵送先) */
        val mailingCity: String?,
        /** 都道府県(郵送先) */
        val mailingState: String?,
        /** 取引先責任者 郵便番号(郵送先) */
        val mailingPostalCode: String?,
        /** 国(郵送先) */
        val mailingCountry: String?,
        /** Mailing Geocode Accuracy */
        val mailingGeocodeAccuracy: String?,
        /** 緯度 */
        val latitude: String?,
        /** 経度 */
        val longitude: String?,
        /** 第 1 SIC コード */
        val primarySic: String?,
        /** 第 1 SIC の説明 */
        val primarySicDesc: String?,
        /** 第 2 SIC コード */
        val secondSic: String?,
        /** 第 2 SIC の説明 */
        val secondSicDesc: String?,
        /** 第 3 SIC コード */
        val thirdSic: String?,
        /** 第 3 SIC の説明 */
        val thirdSicDesc: String?,
        /** 第 4 SIC コード */
        val fourthSic: String?,
        /** 第 4 SIC の説明 */
        val fourthSicDesc: String?,
        /** 第 5 SIC コード */
        val fifthSic: String?,
        /** 第 5 SIC の説明 */
        val fifthSicDesc: String?,
        /** 第 6 SIC コード */
        val sixthSic: String?,
        /** 第 6 SIC の説明 */
        val sixthSicDesc: String?,
        /** 第 1 NAICS コード */
        val primaryNaics: String?,
        /** 第 1 NAICS の説明 */
        val primaryNaicsDesc: String?,
        /** 第 2 NAICS コード */
        val secondNaics: String?,
        /** 第 2 NAICS の説明 */
        val secondNaicsDesc: String?,
        /** 第 3 NAICS コード */
        val thirdNaics: String?,
        /** 第 3 NAICS の説明 */
        val thirdNaicsDesc: String?,
        /** 第 4 NAICS コード */
        val fourthNaics: String?,
        /** 第 4 NAICS の説明 */
        val fourthNaicsDesc: String?,
        /** 第 5 NAICS コード */
        val fifthNaics: String?,
        /** 第 5 NAICS の説明 */
        val fifthNaicsDesc: String?,
        /** 第 6 NAICS コード */
        val sixthNaics: String?,
        /** 第 6 NAICS の説明 */
        val sixthNaicsDesc: String?,
        /** 場所所有者インジケータ */
        val ownOrRent: String?,
        /** 従業員数 - 場所インジケータ */
        val employeesHereReliability: String?,
        /** 年間売上高インジケータ */
        val salesVolumeReliability: String?,
        /** 現地通貨コード */
        val currencyCode: String?,
        /** 法律上の会社形態 */
        val legalStatus: String?,
        /** 従業員数 - 合計インジケータ */
        val employeesTotalReliability: String?,
        /** マイノリティ所有インジケータ */
        val minorityOwned: String?,
        /** 女性所有インジケータ */
        val womenOwned: String?,
        /** 中小企業インジケータ */
        val smallBusiness: String?,
        /** マーケティングセグメンテーションクラスタ */
        val marketingSegmentationCluster: String?,
        /** 輸入/輸出 */
        val importExportAgent: String?,
        /** 関連子会社インジケータ */
        val subsidiary: String?,
        /** 第 2 取引形態 */
        val tradeStyle2: String?,
        /** 第 3 取引形態 */
        val tradeStyle3: String?,
        /** 第 4 取引形態 */
        val tradeStyle4: String?,
        /** 第 5 取引形態 */
        val tradeStyle5: String?,
        /** 国民 ID 番号 */
        val nationalId: String?,
        /** 国民 ID 体系 */
        val nationalIdType: String?,
        /** 米国納税 ID 番号 */
        val usTaxId: String?,
        /** 地理コードの精度 */
        val geoCodeAccuracy: String?,
        /** 支払遅延リスク */
        val marketingPreScreen: String?,
        /** グローバル代表企業の D-U-N-S Number */
        val globalUltimateDunsNumber: String?,
        /** グローバル代表企業の会社名 */
        val globalUltimateBusinessName: String?,
        /** 親会社の D-U-N-S Number */
        val parentOrHqDunsNumber: String?,
        /** 親会社の会社名 */
        val parentOrHqBusinessName: String?,
        /** 国内代表企業の D-U-N-S Number */
        val domesticUltimateDunsNumber: String?,
        /** 国内代表企業の会社名 */
        val domesticUltimateBusinessName: String?,
        /** 場所種別 */
        val locationStatus: String?,
        /** 現地通貨の ISO コード */
        val companyCurrencyIsoCode: String?,
        /** 会社の説明 */
        val description: String?,
        /** S&P 500 */
        val includedInSnP500: String?,
        /** 立地面積の精度 */
        val premisesMeasureReliability: String?,
        /** 立地面積単位 */
        val premisesMeasureUnit: String?,
        /** 第 1 SIC8 コード */
        val primarySic8: String?,
        /** 第 1 SIC8 の説明 */
        val primarySic8Desc: String?,
        /** 第 2 SIC8 コード */
        val secondSic8: String?,
        /** 第 2 SIC8 の説明  */
        val secondSic8Desc: String?,
        /** 第 3 SIC8 コード */
        val thirdSic8: String?,
        /** 第 3 SIC8 の説明 */
        val thirdSic8Desc: String?,
        /** 第 4 SIC8 コード */
        val fourthSic8: String?,
        /** 第 4 SIC8 の説明 */
        val fourthSic8Desc: String?,
        /** 第 5 SIC8 コード */
        val fifthSic8: String?,
        /** 第 5 SIC8 の説明 */
        val fifthSic8Desc: String?,
        /** 第 6 SIC8 コード */
        val sixthSic8: String?,
        /** 第 6 SIC8 の説明 */
        val sixthSic8Desc: String?
) : SObject {
    override fun title(): String = "${super.title()}"
    override fun content(): String = listOfNotNull("").joinToString("\n")
    override val objectType: SObjects = SObjects.DandBCompany
}
