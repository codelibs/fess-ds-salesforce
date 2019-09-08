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
package org.codelibs.fess.ds.salesforce.api.sobject;

import java.util.Arrays;
import java.util.List;

import org.codelibs.fess.ds.salesforce.api.SearchLayout;

/** D&amp;B 企業 */
public class SDandBCompany extends SearchLayout {

    /** D&amp;B 企業 ID */
    protected static final String ID = "Id";
    /** 第 1 会社名 */
    protected static final String NAME = "Name";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";
    /** D-U-N-S number */
    protected static final String DUNS_NUMBER = "DunsNumber";
    /** 住所 */
    protected static final String STREET = "Street";
    /** 市区郡 */
    protected static final String CITY = "City";
    /** 都道府県 */
    protected static final String STATE = "State";
    /** 郵便番号 */
    protected static final String POSTAL_CODE = "PostalCode";
    /** 国 */
    protected static final String COUNTRY = "Country";
    /** Geocode Accuracy */
    protected static final String GEOCODE_ACCURACY_STANDARD = "GeocodeAccuracyStandard";
    /** 電話番号 */
    protected static final String PHONE = "Phone";
    /** Fax 番号 */
    protected static final String FAX = "Fax";
    /** 国際ダイヤルコード */
    protected static final String COUNTRY_ACCESS_CODE = "CountryAccessCode";
    /** 所有者種別インジケータ */
    protected static final String PUBLIC_INDICATOR = "PublicIndicator";
    /** 株式コード */
    protected static final String STOCK_SYMBOL = "StockSymbol";
    /** 証券取引所 */
    protected static final String STOCK_EXCHANGE = "StockExchange";
    /** URL */
    protected static final String URL = "URL";
    /** 倒産インジケータ */
    protected static final String OUT_OF_BUSINESS = "OutOfBusiness";
    /** FIPS MSA コード */
    protected static final String FIPS_MSA_CODE = "FipsMsaCode";
    /** FIPS MSA コードの説明 */
    protected static final String FIPS_MSA_DESC = "FipsMsaDesc";
    /** 第 1 取引形態 */
    protected static final String TRADE_STYLE1 = "TradeStyle1";
    /** 設立年 */
    protected static final String YEAR_STARTED = "YearStarted";
    /** 町名・番地(郵送先) */
    protected static final String MAILING_STREET = "MailingStreet";
    /** 市区郡(郵送先) */
    protected static final String MAILING_CITY = "MailingCity";
    /** 都道府県(郵送先) */
    protected static final String MAILING_STATE = "MailingState";
    /** 取引先責任者 郵便番号(郵送先) */
    protected static final String MAILING_POSTAL_CODE = "MailingPostalCode";
    /** 国(郵送先) */
    protected static final String MAILING_COUNTRY = "MailingCountry";
    /** Mailing Geocode Accuracy */
    protected static final String MAILING_GEOCODE_ACCURACY = "MailingGeocodeAccuracy";
    /** 緯度 */
    protected static final String LATITUDE = "Latitude";
    /** 経度 */
    protected static final String LONGITUDE = "Longitude";
    /** 第 1 SIC コード */
    protected static final String PRIMARY_SIC = "PrimarySic";
    /** 第 1 SIC の説明 */
    protected static final String PRIMARY_SIC_DESC = "PrimarySicDesc";
    /** 第 2 SIC コード */
    protected static final String SECOND_SIC = "SecondSic";
    /** 第 2 SIC の説明 */
    protected static final String SECOND_SIC_DESC = "SecondSicDesc";
    /** 第 3 SIC コード */
    protected static final String THIRD_SIC = "ThirdSic";
    /** 第 3 SIC の説明 */
    protected static final String THIRD_SIC_DESC = "ThirdSicDesc";
    /** 第 4 SIC コード */
    protected static final String FOURTH_SIC = "FourthSic";
    /** 第 4 SIC の説明 */
    protected static final String FOURTH_SIC_DESC = "FourthSicDesc";
    /** 第 5 SIC コード */
    protected static final String FIFTH_SIC = "FifthSic";
    /** 第 5 SIC の説明 */
    protected static final String FIFTH_SIC_DESC = "FifthSicDesc";
    /** 第 6 SIC コード */
    protected static final String SIXTH_SIC = "SixthSic";
    /** 第 6 SIC の説明 */
    protected static final String SIXTH_SIC_DESC = "SixthSicDesc";
    /** 第 1 NAICS コード */
    protected static final String PRIMARY_NAICS = "PrimaryNaics";
    /** 第 1 NAICS の説明 */
    protected static final String PRIMARY_NAICS_DESC = "PrimaryNaicsDesc";
    /** 第 2 NAICS コード */
    protected static final String SECOND_NAICS = "SecondNaics";
    /** 第 2 NAICS の説明 */
    protected static final String SECOND_NAICS_DESC = "SecondNaicsDesc";
    /** 第 3 NAICS コード */
    protected static final String THIRD_NAICS = "ThirdNaics";
    /** 第 3 NAICS の説明 */
    protected static final String THIRD_NAICS_DESC = "ThirdNaicsDesc";
    /** 第 4 NAICS コード */
    protected static final String FOURTH_NAICS = "FourthNaics";
    /** 第 4 NAICS の説明 */
    protected static final String FOURTH_NAICS_DESC = "FourthNaicsDesc";
    /** 第 5 NAICS コード */
    protected static final String FIFTH_NAICS = "FifthNaics";
    /** 第 5 NAICS の説明 */
    protected static final String FIFTH_NAICS_DESC = "FifthNaicsDesc";
    /** 第 6 NAICS コード */
    protected static final String SIXTH_NAICS = "SixthNaics";
    /** 第 6 NAICS の説明 */
    protected static final String SIXTH_NAICS_DESC = "SixthNaicsDesc";
    /** 場所所有者インジケータ */
    protected static final String OWN_OR_RENT = "OwnOrRent";
    /** 従業員数 - 場所インジケータ */
    protected static final String EMPLOYEES_HERE_RELIABILITY = "EmployeesHereReliability";
    /** 年間売上高インジケータ */
    protected static final String SALES_VOLUME_RELIABILITY = "SalesVolumeReliability";
    /** 現地通貨コード */
    protected static final String CURRENCY_CODE = "CurrencyCode";
    /** 法律上の会社形態 */
    protected static final String LEGAL_STATUS = "LegalStatus";
    /** 従業員数 - 合計インジケータ */
    protected static final String EMPLOYEES_TOTAL_RELIABILITY = "EmployeesTotalReliability";
    /** マイノリティ所有インジケータ */
    protected static final String MINORITY_OWNED = "MinorityOwned";
    /** 女性所有インジケータ */
    protected static final String WOMEN_OWNED = "WomenOwned";
    /** 中小企業インジケータ */
    protected static final String SMALL_BUSINESS = "SmallBusiness";
    /** マーケティングセグメンテーションクラスタ */
    protected static final String MARKETING_SEGMENTATION_CLUSTER = "MarketingSegmentationCluster";
    /** 輸入/輸出 */
    protected static final String IMPORT_EXPORT_AGENT = "ImportExportAgent";
    /** 関連子会社インジケータ */
    protected static final String SUBSIDIARY = "Subsidiary";
    /** 第 2 取引形態 */
    protected static final String TRADE_STYLE2 = "TradeStyle2";
    /** 第 3 取引形態 */
    protected static final String TRADE_STYLE3 = "TradeStyle3";
    /** 第 4 取引形態 */
    protected static final String TRADE_STYLE4 = "TradeStyle4";
    /** 第 5 取引形態 */
    protected static final String TRADE_STYLE5 = "TradeStyle5";
    /** 国民 ID 番号 */
    protected static final String NATIONAL_ID = "NationalId";
    /** 国民 ID 体系 */
    protected static final String NATIONAL_ID_TYPE = "NationalIdType";
    /** 米国納税 ID 番号 */
    protected static final String US_TAX_ID = "UsTaxId";
    /** 地理コードの精度 */
    protected static final String GEO_CODE_ACCURACY = "GeoCodeAccuracy";
    /** 支払遅延リスク */
    protected static final String MARKETING_PRE_SCREEN = "MarketingPreScreen";
    /** グローバル代表企業の D-U-N-S Number */
    protected static final String GLOBAL_ULTIMATE_DUNS_NUMBER = "GlobalUltimateDunsNumber";
    /** グローバル代表企業の会社名 */
    protected static final String GLOBAL_ULTIMATE_BUSINESS_NAME = "GlobalUltimateBusinessName";
    /** 親会社の D-U-N-S Number */
    protected static final String PARENT_OR_HQ_DUNS_NUMBER = "ParentOrHqDunsNumber";
    /** 親会社の会社名 */
    protected static final String PARENT_OR_HQ_BUSINESS_NAME = "ParentOrHqBusinessName";
    /** 国内代表企業の D-U-N-S Number */
    protected static final String DOMESTIC_ULTIMATE_DUNS_NUMBER = "DomesticUltimateDunsNumber";
    /** 国内代表企業の会社名 */
    protected static final String DOMESTIC_ULTIMATE_BUSINESS_NAME = "DomesticUltimateBusinessName";
    /** 場所種別 */
    protected static final String LOCATION_STATUS = "LocationStatus";
    /** 現地通貨の ISO コード */
    protected static final String COMPANY_CURRENCY_ISO_CODE = "CompanyCurrencyIsoCode";
    /** 会社の説明 */
    protected static final String DESCRIPTION = "Description";
    /** S&amp;P 500 */
    protected static final String INCLUDED_IN_SN_P500 = "IncludedInSnP500";
    /** 立地面積の精度 */
    protected static final String PREMISES_MEASURE_RELIABILITY = "PremisesMeasureReliability";
    /** 立地面積単位 */
    protected static final String PREMISES_MEASURE_UNIT = "PremisesMeasureUnit";
    /** 第 1 SIC8 コード */
    protected static final String PRIMARY_SIC8 = "PrimarySic8";
    /** 第 1 SIC8 の説明 */
    protected static final String PRIMARY_SIC8_DESC = "PrimarySic8Desc";
    /** 第 2 SIC8 コード */
    protected static final String SECOND_SIC8 = "SecondSic8";
    /** 第 2 SIC8 の説明  */
    protected static final String SECOND_SIC8_DESC = "SecondSic8Desc";
    /** 第 3 SIC8 コード */
    protected static final String THIRD_SIC8 = "ThirdSic8";
    /** 第 3 SIC8 の説明 */
    protected static final String THIRD_SIC8_DESC = "ThirdSic8Desc";
    /** 第 4 SIC8 コード */
    protected static final String FOURTH_SIC8 = "FourthSic8";
    /** 第 4 SIC8 の説明 */
    protected static final String FOURTH_SIC8_DESC = "FourthSic8Desc";
    /** 第 5 SIC8 コード */
    protected static final String FIFTH_SIC8 = "FifthSic8";
    /** 第 5 SIC8 の説明 */
    protected static final String FIFTH_SIC8_DESC = "FifthSic8Desc";
    /** 第 6 SIC8 コード */
    protected static final String SIXTH_SIC8 = "SixthSic8";
    /** 第 6 SIC8 の説明 */
    protected static final String SIXTH_SIC8_DESC = "SixthSic8Desc";

    protected static final List<String> contents = Arrays.asList(NAME, DUNS_NUMBER, PHONE, FAX, URL, LOCATION_STATUS, DESCRIPTION);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
