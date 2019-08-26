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

/** 注文 */
class SOrder extends SearchLayout {

    String title = Field.Name.name();
    List<String> contents = Arrays.asList(Field.Status, Field.Description, Field.Type,
            Field.Name, Field.OrderNumber)
            .stream().map(o -> o.name()).collect(Collectors.toList());

    enum Field {
        /** 注文 ID */
        Id,
        /** 注文開始日 */
        EffectiveDate,
        /** 注文終了日 */
        EndDate,
        /** 状況 */
        Status,
        /** 説明 */
        Description,
        /** 顧客 承認日 */
        CustomerAuthorizedDate,
        /** 自社 承認日 */
        CompanyAuthorizedDate,
        /** 注文種別 */
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
        /** 注文名 */
        Name,
        /** PO 日付 */
        PoDate,
        /** PO 番号 */
        PoNumber,
        /** 注文参照番号 */
        OrderReferenceNumber,
        /** 有効化日 */
        ActivatedDate,
        /** 状況のカテゴリ */
        StatusCode,
        /** 注文番号 */
        OrderNumber,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate
    }
}
