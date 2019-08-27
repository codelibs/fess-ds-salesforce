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

import org.codelibs.fess.ds.salesforce.api.SearchLayout;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** グループ */
class SCollaborationGroup extends SearchLayout {

    String title = Field.Name.name();
    List<String> contents = Arrays.asList(Field.Name, Field.Description, Field.InformationTitle,
            Field.InformationBody, Field.GroupEmail)
            .stream().map(o -> o.name()).collect(Collectors.toList());
    String thumbnail = Field.SmallPhotoUrl.name();

    enum Field {
        /** グループ ID */
        Id,
        /** 名前 */
        Name,
        /** アクセス種別 */
        CollaborationType,
        /** 説明 */
        Description,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 実寸大の写真の URL */
        FullPhotoUrl,
        /** プロファイル写真 (中) の URL */
        MediumPhotoUrl,
        /** 写真 */
        SmallPhotoUrl,
        /** 最終フィード更新日 */
        LastFeedModifiedDate,
        /** 情報のタイトル */
        InformationTitle,
        /** 情報 */
        InformationBody,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** グループのメール */
        GroupEmail,
        /** バナー写真の URL */
        BannerPhotoUrl
    }
}