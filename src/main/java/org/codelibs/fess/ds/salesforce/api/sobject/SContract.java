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

/** 契約 */
public class SContract extends SearchLayout {

    protected static final String title = Field.ContractNumber.name();
    protected static final List<String> contents = Stream.of(Field.Status, Field.Description, Field.ContractNumber)
            .map(Enum::name).collect(Collectors.toList());

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

    private enum Field {
        /** 契約 ID */
        Id,
        /** 所有者に対する終了通知 */
        OwnerExpirationNotice,
        /** 契約開始日 */
        StartDate,
        /** 契約終了日 */
        EndDate,
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
        /** 状況 */
        Status,
        /** 自社 契約日 */
        CompanySignedDate,
        /** 顧客 調印者役職 */
        CustomerSignedTitle,
        /** 顧客 契約日 */
        CustomerSignedDate,
        /** 特記事項 */
        SpecialTerms,
        /** 有効日 */
        ActivatedDate,
        /** 状況のカテゴリ */
        StatusCode,
        /** 説明 */
        Description,
        /** 契約番号 */
        ContractNumber,
        /** 最終承認日 */
        LastApprovedDate,
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
        LastReferencedDate
    }
}
