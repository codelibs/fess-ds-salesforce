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


/** 注文 */
public class SOrder extends SearchLayout {

    /** 注文 ID */
    protected static final String ID = "Id";
    /** 注文開始日 */
    protected static final String EFFECTIVE_DATE = "EffectiveDate";
    /** 注文終了日 */
    protected static final String END_DATE = "EndDate";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 顧客 承認日 */
    protected static final String CUSTOMER_AUTHORIZED_DATE = "CustomerAuthorizedDate";
    /** 自社 承認日 */
    protected static final String COMPANY_AUTHORIZED_DATE = "CompanyAuthorizedDate";
    /** 注文種別 */
    protected static final String TYPE = "Type";
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
    /** 町名・番地(納入先) */
    protected static final String SHIPPING_STREET = "ShippingStreet";
    /** 市区郡(納入先) */
    protected static final String SHIPPING_CITY = "ShippingCity";
    /** 都道府県(納入先) */
    protected static final String SHIPPING_STATE = "ShippingState";
    /** 郵便番号(納入先) */
    protected static final String SHIPPING_POSTAL_CODE = "ShippingPostalCode";
    /** 国(納入先) */
    protected static final String SHIPPING_COUNTRY = "ShippingCountry";
    /** Shipping Geocode Accuracy */
    protected static final String SHIPPING_GEOCODE_ACCURACY = "ShippingGeocodeAccuracy";
    /** 注文名 */
    protected static final String NAME = "Name";
    /** PO 日付 */
    protected static final String PO_DATE = "PoDate";
    /** PO 番号 */
    protected static final String PO_NUMBER = "PoNumber";
    /** 注文参照番号 */
    protected static final String ORDER_REFERENCE_NUMBER = "OrderReferenceNumber";
    /** 有効化日 */
    protected static final String ACTIVATED_DATE = "ActivatedDate";
    /** 状況のカテゴリ */
    protected static final String STATUS_CODE = "StatusCode";
    /** 注文番号 */
    protected static final String ORDER_NUMBER = "OrderNumber";
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

    protected static final List<String> contents = Arrays.asList(STATUS, DESCRIPTION, TYPE, NAME, ORDER_NUMBER);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
