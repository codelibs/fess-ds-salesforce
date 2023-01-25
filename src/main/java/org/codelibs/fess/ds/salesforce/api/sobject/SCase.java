/*
 * Copyright 2012-2023 CodeLibs Project and the Others.
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

/** ケース */
public class SCase extends SearchLayout {

    /** ケース ID */
    protected static final String ID = "Id";
    /** ケース番号 */
    protected static final String CASE_NUMBER = "CaseNumber";
    /** 名前 */
    protected static final String SUPPLIED_NAME = "SuppliedName";
    /** メールアドレス */
    protected static final String SUPPLIED_EMAIL = "SuppliedEmail";
    /** 電話 */
    protected static final String SUPPLIED_PHONE = "SuppliedPhone";
    /** 会社名 */
    protected static final String SUPPLIED_COMPANY = "SuppliedCompany";
    /** ケース種別 */
    protected static final String TYPE = "Type";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 原因 */
    protected static final String REASON = "Reason";
    /** 発生源 */
    protected static final String ORIGIN = "Origin";
    /** 件名 */
    protected static final String SUBJECT = "Subject";
    /** 優先度 */
    protected static final String PRIORITY = "Priority";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 完了日 */
    protected static final String CLOSED_DATE = "ClosedDate";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 取引先責任者 電話 */
    protected static final String CONTACT_PHONE = "ContactPhone";
    /** 取引先責任者 携帯 */
    protected static final String CONTACT_MOBILE = "ContactMobile";
    /** 取引先責任者 メール */
    protected static final String CONTACT_EMAIL = "ContactEmail";
    /** 取引先責任者 Fax */
    protected static final String CONTACT_FAX = "ContactFax";
    /** 社内コメント */
    protected static final String COMMENTS = "Comments";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";

    protected static final List<String> contents =
            Arrays.asList(CASE_NUMBER, SUPPLIED_NAME, SUPPLIED_EMAIL, SUPPLIED_PHONE, SUPPLIED_COMPANY, TYPE, STATUS, REASON, ORIGIN,
                    SUBJECT, DESCRIPTION, CONTACT_PHONE, CONTACT_MOBILE, CONTACT_EMAIL, CONTACT_FAX, COMMENTS);

    @Override
    public String getTitle() {
        return SUBJECT;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
