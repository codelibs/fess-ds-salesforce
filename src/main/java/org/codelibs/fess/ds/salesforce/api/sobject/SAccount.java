/*
 * Copyright 2012-2022 CodeLibs Project and the Others.
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

/** 取引先 */
public class SAccount extends SearchLayout {

    /** 取引先 ID */
    protected static final String ID = "Id";
    /** 取引先名 */
    protected static final String NAME = "Name";
    /** 取引先 種別 */
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
    /** 取引先 電話 */
    protected static final String PHONE = "Phone";
    /** 取引先 Fax */
    protected static final String FAX = "Fax";
    /** 取引先番号 */
    protected static final String ACCOUNT_NUMBER = "AccountNumber";
    /** Web サイト */
    protected static final String WEBSITE = "Website";
    /** 写真の URL */
    protected static final String PHOTO_URL = "PhotoUrl";
    /** 産業コード */
    protected static final String SIC = "Sic";
    /** 業種 */
    protected static final String INDUSTRY = "Industry";
    /** 会社形態 */
    protected static final String OWNERSHIP = "Ownership";
    /** 株式コード */
    protected static final String TICKER_SYMBOL = "TickerSymbol";
    /** 取引先 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 取引先 評価 */
    protected static final String RATING = "Rating";
    /** 取引先 部門 */
    protected static final String SITE = "Site";
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
    /** Data.com キー */
    protected static final String JIGSAW = "Jigsaw";
    /** Jigsaw Company ID */
    protected static final String JIGSAW_COMPANY_ID = "JigsawCompanyId";
    /** 状況をクリーンアップ */
    protected static final String CLEAN_STATUS = "CleanStatus";
    /** 取引先ソース */
    protected static final String ACCOUNT_SOURCE = "AccountSource";
    /** D-U-N-S 番号 */
    protected static final String DUNS_NUMBER = "DunsNumber";
    /** 取引形態 */
    protected static final String TRADESTYLE = "Tradestyle";
    /** NAICS コード */
    protected static final String NAICS_CODE = "NaicsCode";
    /** NAICS の説明 */
    protected static final String NAICS_DESC = "NaicsDesc";
    /** 開始年 */
    protected static final String YEAR_STARTED = "YearStarted";
    /** 産業区分の説明 */
    protected static final String SIC_DESC = "SicDesc";

    protected static final List<String> contents =
            Arrays.asList(NAME, TYPE, PHONE, FAX, WEBSITE, TICKER_SYMBOL, DESCRIPTION, SITE, DUNS_NUMBER);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
