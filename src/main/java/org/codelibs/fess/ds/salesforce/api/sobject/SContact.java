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

/** 取引先責任者 */
public class SContact extends SearchLayout {

    /** 取引先責任者 ID */
    protected static final String ID = "Id";
    /** 姓 */
    protected static final String LAST_NAME = "LastName";
    /** 名 */
    protected static final String FIRST_NAME = "FirstName";
    /** 敬称 */
    protected static final String SALUTATION = "Salutation";
    /** 氏名 */
    protected static final String NAME = "Name";
    /** 町名・番地(その他) */
    protected static final String OTHER_STREET = "OtherStreet";
    /** 市区郡(その他) */
    protected static final String OTHER_CITY = "OtherCity";
    /** 都道府県(その他) */
    protected static final String OTHER_STATE = "OtherState";
    /** 郵便番号(その他) */
    protected static final String OTHER_POSTAL_CODE = "OtherPostalCode";
    /** 国(その他) */
    protected static final String OTHER_COUNTRY = "OtherCountry";
    /** Other Geocode Accuracy */
    protected static final String OTHER_GEOCODE_ACCURACY = "OtherGeocodeAccuracy";
    /** 町名・番地(郵送先) */
    protected static final String MAILING_STREET = "MailingStreet";
    /** 市区郡(郵送先) */
    protected static final String MAILING_CITY = "MailingCity";
    /** 都道府県(郵送先) */
    protected static final String MAILING_STATE = "MailingState";
    /** 郵便番号(郵送先) */
    protected static final String MAILING_POSTAL_CODE = "MailingPostalCode";
    /** 国(郵送先) */
    protected static final String MAILING_COUNTRY = "MailingCountry";
    /** Mailing Geocode Accuracy */
    protected static final String MAILING_GEOCODE_ACCURACY = "MailingGeocodeAccuracy";
    /** 電話 */
    protected static final String PHONE = "Phone";
    /** Fax */
    protected static final String FAX = "Fax";
    /** 携帯電話 */
    protected static final String MOBILE_PHONE = "MobilePhone";
    /** 自宅電話 */
    protected static final String HOME_PHONE = "HomePhone";
    /** その他の電話 */
    protected static final String OTHER_PHONE = "OtherPhone";
    /** アシスタント電話 */
    protected static final String ASSISTANT_PHONE = "AssistantPhone";
    /** メール */
    protected static final String EMAIL = "Email";
    /** 役職 */
    protected static final String TITLE = "Title";
    /** 部署 */
    protected static final String DEPARTMENT = "Department";
    /** アシスタント名 */
    protected static final String ASSISTANT_NAME = "AssistantName";
    /** リードソース */
    protected static final String LEAD_SOURCE = "LeadSource";
    /** 誕生日 */
    protected static final String BIRTHDATE = "Birthdate";
    /** 取引先責任者 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 最終活動日 */
    protected static final String LAST_ACTIVITY_DATE = "LastActivityDate";
    /** 登録情報照会 最終依頼日 */
    protected static final String LAST_CU_REQUEST_DATE = "LastCURequestDate";
    /** 登録情報照会 最終更新日 */
    protected static final String LAST_CU_UPDATE_DATE = "LastCUUpdateDate";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";
    /** メール不達の理由 */
    protected static final String EMAIL_BOUNCED_REASON = "EmailBouncedReason";
    /** メール不達発生日 */
    protected static final String EMAIL_BOUNCED_DATE = "EmailBouncedDate";
    /** 写真の URL */
    protected static final String PHOTO_URL = "PhotoUrl";
    /** Data.com キー */
    protected static final String JIGSAW = "Jigsaw";
    /** Jigsaw Contact ID */
    protected static final String JIGSAW_CONTACT_ID = "JigsawContactId";
    /** 状況をクリーンアップ */
    protected static final String CLEAN_STATUS = "CleanStatus";

    protected static final List<String> contents =
            Arrays.asList(NAME, PHONE, FAX, MOBILE_PHONE, HOME_PHONE, OTHER_PHONE, EMAIL, TITLE, DEPARTMENT, DESCRIPTION);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
