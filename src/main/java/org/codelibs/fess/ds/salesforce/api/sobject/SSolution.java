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

/** ソリューション */
public class SSolution extends SearchLayout {

    /** ソリューション ID */
    protected static final String ID = "Id";
    /** ソリューション 番号 */
    protected static final String SOLUTION_NUMBER = "SolutionNumber";
    /** ソリューション名 */
    protected static final String SOLUTION_NAME = "SolutionName";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 説明 */
    protected static final String SOLUTION_NOTE = "SolutionNote";
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

    protected static final List<String> contents = Arrays.asList(SOLUTION_NAME, STATUS, SOLUTION_NOTE);

    @Override
    public String getTitle() {
        return SOLUTION_NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
