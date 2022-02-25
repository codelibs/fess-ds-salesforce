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

/** 商談 */
public class SOpportunity extends SearchLayout {

    /** 商談 ID */
    protected static final String ID = "Id";
    /** 商談名 */
    protected static final String NAME = "Name";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** フェーズ */
    protected static final String STAGE_NAME = "StageName";
    /** 完了予定日 */
    protected static final String CLOSE_DATE = "CloseDate";
    /** 商談 種別 */
    protected static final String TYPE = "Type";
    /** 次のステップ */
    protected static final String NEXT_STEP = "NextStep";
    /** リードソース */
    protected static final String LEAD_SOURCE = "LeadSource";
    /** 売上予測分類 */
    protected static final String FORECAST_CATEGORY = "ForecastCategory";
    /** 売上予測分類 */
    protected static final String FORECAST_CATEGORY_NAME = "ForecastCategoryName";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 最終活動日 */
    protected static final String LAST_ACTIVITY_DATE = "LastActivityDate";
    /** 会計期間 */
    protected static final String FISCAL = "Fiscal";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";

    protected static final List<String> contents = Arrays.asList(NAME, DESCRIPTION, STAGE_NAME, TYPE, NEXT_STEP);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
