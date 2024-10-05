/*
 * Copyright 2012-2024 CodeLibs Project and the Others.
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

/** 契約 */
public class SContract extends SearchLayout {

    /** 契約 ID */
    protected static final String ID = "Id";
    /** 所有者に対する終了通知 */
    protected static final String OWNER_EXPIRATION_NOTICE = "OwnerExpirationNotice";
    /** 契約開始日 */
    protected static final String START_DATE = "StartDate";
    /** 契約終了日 */
    protected static final String END_DATE = "EndDate";
    /** 町名・番地(請求先) */
    protected static final String BILLING_STREET = "BillingStreet";
    /** 市区郡(請求先) */
    protected static final String BILLING_CITY = "BillingCity";
    /** 都道府県(請求先) */
    protected static final String BILLING_STATE = "BillingState";
    /** 郵便番号(請求先) */
    protected static final String BILLING_POSTAL_CODE = "BillingPostalCode";
    /** 国(請求先) */
    protected static final String BILLING_COUNTRY = "BillingCountry";
    /** Billing Geocode Accuracy */
    protected static final String BILLING_GEOCODE_ACCURACY = "BillingGeocodeAccuracy";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 自社 契約日 */
    protected static final String COMPANY_SIGNED_DATE = "CompanySignedDate";
    /** 顧客 調印者役職 */
    protected static final String CUSTOMER_SIGNED_TITLE = "CustomerSignedTitle";
    /** 顧客 契約日 */
    protected static final String CUSTOMER_SIGNED_DATE = "CustomerSignedDate";
    /** 特記事項 */
    protected static final String SPECIAL_TERMS = "SpecialTerms";
    /** 有効日 */
    protected static final String ACTIVATED_DATE = "ActivatedDate";
    /** 状況のカテゴリ */
    protected static final String STATUS_CODE = "StatusCode";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 契約番号 */
    protected static final String CONTRACT_NUMBER = "ContractNumber";
    /** 最終承認日 */
    protected static final String LAST_APPROVED_DATE = "LastApprovedDate";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 最終活動日 */
    protected static final String LAST_ACTIVITY_DATE = "LastActivityDate";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";

    protected static final List<String> contents = Arrays.asList(STATUS, DESCRIPTION, CONTRACT_NUMBER);

    @Override
    public String getTitle() {
        return CONTRACT_NUMBER;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
