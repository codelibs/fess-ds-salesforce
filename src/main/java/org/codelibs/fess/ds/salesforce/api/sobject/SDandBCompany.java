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

import org.codelibs.fess.ds.salesforce.api.SearchLayout;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** D&B 企業 */
class SDandBCompany extends SearchLayout {

    String title = Field.Name.name();
    List<String> contents = Arrays.asList(Field.Name, Field.DunsNumber, Field.Phone,
            Field.Fax, Field.URL, Field.LocationStatus, Field.Description)
            .stream().map(o -> o.name()).collect(Collectors.toList());

    public SDandBCompany() {

    }

    enum Field {
        /** D&B 企業 ID */
        Id,
        /** 第 1 会社名 */
        Name,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** D-U-N-S number */
        DunsNumber,
        /** 住所 */
        Street,
        /** 市区郡 */
        City,
        /** 都道府県 */
        State,
        /** 郵便番号 */
        PostalCode,
        /** 国 */
        Country,
        /** Geocode Accuracy */
        GeocodeAccuracyStandard,
        /** 電話番号 */
        Phone,
        /** Fax 番号 */
        Fax,
        /** 国際ダイヤルコード */
        CountryAccessCode,
        /** 所有者種別インジケータ */
        PublicIndicator,
        /** 株式コード */
        StockSymbol,
        /** 証券取引所 */
        StockExchange,
        /** URL */
        URL,
        /** 倒産インジケータ */
        OutOfBusiness,
        /** FIPS MSA コード */
        FipsMsaCode,
        /** FIPS MSA コードの説明 */
        FipsMsaDesc,
        /** 第 1 取引形態 */
        TradeStyle1,
        /** 設立年 */
        YearStarted,
        /** 町名・番地(郵送先) */
        MailingStreet,
        /** 市区郡(郵送先) */
        MailingCity,
        /** 都道府県(郵送先) */
        MailingState,
        /** 取引先責任者 郵便番号(郵送先) */
        MailingPostalCode,
        /** 国(郵送先) */
        MailingCountry,
        /** Mailing Geocode Accuracy */
        MailingGeocodeAccuracy,
        /** 緯度 */
        Latitude,
        /** 経度 */
        Longitude,
        /** 第 1 SIC コード */
        PrimarySic,
        /** 第 1 SIC の説明 */
        PrimarySicDesc,
        /** 第 2 SIC コード */
        SecondSic,
        /** 第 2 SIC の説明 */
        SecondSicDesc,
        /** 第 3 SIC コード */
        ThirdSic,
        /** 第 3 SIC の説明 */
        ThirdSicDesc,
        /** 第 4 SIC コード */
        FourthSic,
        /** 第 4 SIC の説明 */
        FourthSicDesc,
        /** 第 5 SIC コード */
        FifthSic,
        /** 第 5 SIC の説明 */
        FifthSicDesc,
        /** 第 6 SIC コード */
        SixthSic,
        /** 第 6 SIC の説明 */
        SixthSicDesc,
        /** 第 1 NAICS コード */
        PrimaryNaics,
        /** 第 1 NAICS の説明 */
        PrimaryNaicsDesc,
        /** 第 2 NAICS コード */
        SecondNaics,
        /** 第 2 NAICS の説明 */
        SecondNaicsDesc,
        /** 第 3 NAICS コード */
        ThirdNaics,
        /** 第 3 NAICS の説明 */
        ThirdNaicsDesc,
        /** 第 4 NAICS コード */
        FourthNaics,
        /** 第 4 NAICS の説明 */
        FourthNaicsDesc,
        /** 第 5 NAICS コード */
        FifthNaics,
        /** 第 5 NAICS の説明 */
        FifthNaicsDesc,
        /** 第 6 NAICS コード */
        SixthNaics,
        /** 第 6 NAICS の説明 */
        SixthNaicsDesc,
        /** 場所所有者インジケータ */
        OwnOrRent,
        /** 従業員数 - 場所インジケータ */
        EmployeesHereReliability,
        /** 年間売上高インジケータ */
        SalesVolumeReliability,
        /** 現地通貨コード */
        CurrencyCode,
        /** 法律上の会社形態 */
        LegalStatus,
        /** 従業員数 - 合計インジケータ */
        EmployeesTotalReliability,
        /** マイノリティ所有インジケータ */
        MinorityOwned,
        /** 女性所有インジケータ */
        WomenOwned,
        /** 中小企業インジケータ */
        SmallBusiness,
        /** マーケティングセグメンテーションクラスタ */
        MarketingSegmentationCluster,
        /** 輸入/輸出 */
        ImportExportAgent,
        /** 関連子会社インジケータ */
        Subsidiary,
        /** 第 2 取引形態 */
        TradeStyle2,
        /** 第 3 取引形態 */
        TradeStyle3,
        /** 第 4 取引形態 */
        TradeStyle4,
        /** 第 5 取引形態 */
        TradeStyle5,
        /** 国民 ID 番号 */
        NationalId,
        /** 国民 ID 体系 */
        NationalIdType,
        /** 米国納税 ID 番号 */
        UsTaxId,
        /** 地理コードの精度 */
        GeoCodeAccuracy,
        /** 支払遅延リスク */
        MarketingPreScreen,
        /** グローバル代表企業の D-U-N-S Number */
        GlobalUltimateDunsNumber,
        /** グローバル代表企業の会社名 */
        GlobalUltimateBusinessName,
        /** 親会社の D-U-N-S Number */
        ParentOrHqDunsNumber,
        /** 親会社の会社名 */
        ParentOrHqBusinessName,
        /** 国内代表企業の D-U-N-S Number */
        DomesticUltimateDunsNumber,
        /** 国内代表企業の会社名 */
        DomesticUltimateBusinessName,
        /** 場所種別 */
        LocationStatus,
        /** 現地通貨の ISO コード */
        CompanyCurrencyIsoCode,
        /** 会社の説明 */
        Description,
        /** S&P 500 */
        IncludedInSnP500,
        /** 立地面積の精度 */
        PremisesMeasureReliability,
        /** 立地面積単位 */
        PremisesMeasureUnit,
        /** 第 1 SIC8 コード */
        PrimarySic8,
        /** 第 1 SIC8 の説明 */
        PrimarySic8Desc,
        /** 第 2 SIC8 コード */
        SecondSic8,
        /** 第 2 SIC8 の説明  */
        SecondSic8Desc,
        /** 第 3 SIC8 コード */
        ThirdSic8,
        /** 第 3 SIC8 の説明 */
        ThirdSic8Desc,
        /** 第 4 SIC8 コード */
        FourthSic8,
        /** 第 4 SIC8 の説明 */
        FourthSic8Desc,
        /** 第 5 SIC8 コード */
        FifthSic8,
        /** 第 5 SIC8 の説明 */
        FifthSic8Desc,
        /** 第 6 SIC8 コード */
        SixthSic8,
        /** 第 6 SIC8 の説明 */
        SixthSic8Desc
    }
}
