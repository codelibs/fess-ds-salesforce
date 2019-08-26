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

/** アイデア */
class SIdea extends SearchLayout {

    String title = Field.Title.name();
    List<String> contents = Arrays.asList(Field.Title, Field.Body, Field.Categories,
            Field.Status, Field.CreatorName)
            .stream().map(o -> o.name()).collect(Collectors.toList());

    enum Field {
        /** アイデア ID */
        Id,
        /** タイトル */
        Title,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** アイデア本文 */
        Body,
        /** カテゴリ */
        Categories,
        /** 状況 */
        Status,
        /** 最終アイデアのコメント日 */
        LastCommentDate,
        /** 作成者のプロファイル写真の URL */
        CreatorFullPhotoUrl,
        /** 作成者の写真のサムネイルの URL */
        CreatorSmallPhotoUrl,
        /** 作成者の名前 */
        CreatorName
    }
}
