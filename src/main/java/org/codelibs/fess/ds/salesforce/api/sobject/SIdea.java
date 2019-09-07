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

/** アイデア */
public class SIdea extends SearchLayout {

    /** アイデア ID */
    protected static final String ID = "Id";
    /** タイトル */
    protected static final String TITLE = "Title";
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
    /** アイデア本文 */
    protected static final String BODY = "Body";
    /** カテゴリ */
    protected static final String CATEGORIES = "Categories";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 最終アイデアのコメント日 */
    protected static final String LAST_COMMENT_DATE = "LastCommentDate";
    /** 作成者のプロファイル写真の URL */
    protected static final String CREATOR_FULL_PHOTO_URL = "CreatorFullPhotoUrl";
    /** 作成者の写真のサムネイルの URL */
    protected static final String CREATOR_SMALL_PHOTO_URL = "CreatorSmallPhotoUrl";
    /** 作成者の名前 */
    protected static final String CREATOR_NAME = "CreatorName";

    protected static final String title = TITLE;

    protected static final List<String> contents = Arrays.asList(TITLE, BODY, CATEGORIES, STATUS, CREATOR_NAME);

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
