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

/** グループ */
public class SCollaborationGroup extends SearchLayout {

    /** グループ ID */
    protected static final String ID = "Id";
    /** 名前 */
    protected static final String NAME = "Name";
    /** アクセス種別 */
    protected static final String COLLABORATION_TYPE = "CollaborationType";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 実寸大の写真の URL */
    protected static final String FULL_PHOTO_URL = "FullPhotoUrl";
    /** プロファイル写真 (中) の URL */
    protected static final String MEDIUM_PHOTO_URL = "MediumPhotoUrl";
    /** 写真 */
    protected static final String SMALL_PHOTO_URL = "SmallPhotoUrl";
    /** 最終フィード更新日 */
    protected static final String LAST_FEED_MODIFIED_DATE = "LastFeedModifiedDate";
    /** 情報のタイトル */
    protected static final String INFORMATION_TITLE = "InformationTitle";
    /** 情報 */
    protected static final String INFORMATION_BODY = "InformationBody";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";
    /** グループのメール */
    protected static final String GROUP_EMAIL = "GroupEmail";
    /** バナー写真の URL */
    protected static final String BANNER_PHOTO_URL = "BannerPhotoUrl";

    protected static final List<String> contents = Arrays.asList(NAME, DESCRIPTION, INFORMATION_TITLE, INFORMATION_BODY, GROUP_EMAIL);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

    @Override
    public String getThumbnail() {
        return SMALL_PHOTO_URL;
    }

}
