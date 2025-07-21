/*
 * Copyright 2012-2025 CodeLibs Project and the Others.
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

/**
 * Represents the layout for the Campaign SObject in Salesforce.
 */
public class SCampaign extends SearchLayout {

    /**
     * Default constructor.
     */
    public SCampaign() {
        // Do nothing
    }

    /** キャンペーン ID */
    protected static final String ID = "Id";
    /** 名前 */
    protected static final String NAME = "Name";
    /** 種別 */
    protected static final String TYPE = "Type";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 開始日 */
    protected static final String START_DATE = "StartDate";
    /** 終了日 */
    protected static final String END_DATE = "EndDate";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
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

    /**
     * List of fields to be used for the main content of the search document.
     */
    protected static final List<String> contents = Arrays.asList(NAME, TYPE, STATUS, DESCRIPTION);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}