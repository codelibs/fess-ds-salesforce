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

/** リストメール */
public class SListEmail extends SearchLayout {

    /** リストメール ID */
    protected static final String ID = "Id";
    /** 名前 */
    protected static final String NAME = "Name";
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
    /** 件名 */
    protected static final String SUBJECT = "Subject";
    /** HTML 内容 */
    protected static final String HTML_BODY = "HtmlBody";
    /** テキスト内容 */
    protected static final String TEXT_BODY = "TextBody";
    /** 差出人名 */
    protected static final String FROM_NAME = "FromName";
    /** 送信元アドレス */
    protected static final String FROM_ADDRESS = "FromAddress";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 予定日 */
    protected static final String SCHEDULED_DATE = "ScheduledDate";

    protected static final List<String> contents = Arrays.asList(NAME, SUBJECT, TEXT_BODY, FROM_NAME, FROM_ADDRESS, STATUS);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
