/*
 * Copyright 2012-2021 CodeLibs Project and the Others.
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

/** リード */
public class SLead extends SearchLayout {

    /** リード ID */
    protected static final String ID = "Id";
    /** 姓 */
    protected static final String LAST_NAME = "LastName";
    /** 名 */
    protected static final String FIRST_NAME = "FirstName";
    /** 敬称 */
    protected static final String SALUTATION = "Salutation";
    /** 氏名 */
    protected static final String NAME = "Name";
    /** 役職 */
    protected static final String TITLE = "Title";
    /** 会社名 */
    protected static final String COMPANY = "Company";
    /** 町名・番地 */
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
    protected static final String GEOCODE_ACCURACY = "GeocodeAccuracy";
    /** 電話 */
    protected static final String PHONE = "Phone";
    /** 携帯電話 */
    protected static final String MOBILE_PHONE = "MobilePhone";
    /** Fax */
    protected static final String FAX = "Fax";
    /** メール */
    protected static final String EMAIL = "Email";
    /** Web サイト */
    protected static final String WEBSITE = "Website";
    /** 写真の URL */
    protected static final String PHOTO_URL = "PhotoUrl";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** リードソース */
    protected static final String LEAD_SOURCE = "LeadSource";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 業種 */
    protected static final String INDUSTRY = "Industry";
    /** 評価 */
    protected static final String RATING = "Rating";
    /** 取引開始日 */
    protected static final String CONVERTED_DATE = "ConvertedDate";
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
    /** Jigsaw Contact ID */
    protected static final String JIGSAW_CONTACT_ID = "JigsawContactId";
    /** 状況をクリーンアップ */
    protected static final String CLEAN_STATUS = "CleanStatus";
    /** 会社 D-U-N-S 番号 */
    protected static final String COMPANY_DUNS_NUMBER = "CompanyDunsNumber";
    /** メール不達の理由 */
    protected static final String EMAIL_BOUNCED_REASON = "EmailBouncedReason";
    /** メール不達発生日 */
    protected static final String EMAIL_BOUNCED_DATE = "EmailBouncedDate";

    protected static final List<String> contents =
            Arrays.asList(NAME, TITLE, COMPANY, PHONE, MOBILE_PHONE, FAX, EMAIL, WEBSITE, DESCRIPTION, STATUS, INDUSTRY);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
