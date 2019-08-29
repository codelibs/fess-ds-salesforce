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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codelibs.fess.ds.salesforce.api.SearchLayout;

/** 取引先 */
public class SAccount extends SearchLayout {

    protected static final String title = Field.Name.name();
    protected static final List<String> contents = Stream.of(Field.Name, Field.Type, Field.Phone,
            Field.Fax, Field.Website, Field.TickerSymbol, Field.Description, Field.Site, Field.DunsNumber).map(Enum::name).collect(Collectors.toList());

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

    private enum Field {
        /** 取引先 ID */
        Id,
        /** 取引先名 */
        Name,
        /** 取引先 種別 */
        Type,
        /** 町名・番地(請求先) */
        BillingStreet,
        /** 市区郡(請求先) */
        BillingCity,
        /** 都道府県(請求先) */
        BillingState,
        /** 郵便番号(請求先) */
        BillingPostalCode,
        /** 国(請求先) */
        BillingCountry,
        /** Billing Geocode Accuracy */
        BillingGeocodeAccuracy,
        /** 町名・番地(納入先) */
        ShippingStreet,
        /** 市区郡(納入先) */
        ShippingCity,
        /** 都道府県(納入先) */
        ShippingState,
        /** 郵便番号(納入先) */
        ShippingPostalCode,
        /** 国(納入先) */
        ShippingCountry,
        /** Shipping Geocode Accuracy */
        ShippingGeocodeAccuracy,
        /** 取引先 電話 */
        Phone,
        /** 取引先 Fax */
        Fax,
        /** 取引先番号 */
        AccountNumber,
        /** Web サイト */
        Website,
        /** 写真の URL */
        PhotoUrl,
        /** 産業コード */
        Sic,
        /** 業種 */
        Industry,
        /** 会社形態 */
        Ownership,
        /** 株式コード */
        TickerSymbol,
        /** 取引先 説明 */
        Description,
        /** 取引先 評価 */
        Rating,
        /** 取引先 部門 */
        Site,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 最終活動日 */
        LastActivityDate,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** Data.com キー */
        Jigsaw,
        /** Jigsaw Company ID */
        JigsawCompanyId,
        /** 状況をクリーンアップ */
        CleanStatus,
        /** 取引先ソース */
        AccountSource,
        /** D-U-N-S 番号 */
        DunsNumber,
        /** 取引形態 */
        Tradestyle,
        /** NAICS コード */
        NaicsCode,
        /** NAICS の説明 */
        NaicsDesc,
        /** 開始年 */
        YearStarted,
        /** 産業区分の説明 */
        SicDesc
    }
}
