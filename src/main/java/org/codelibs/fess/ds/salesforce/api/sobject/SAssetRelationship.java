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

/** 納入商品リレーション */
class SAssetRelationship extends SearchLayout {

    String title = Field.AssetRelationshipNumber.name();
    List<String> contents = Arrays.asList(Field.AssetRelationshipNumber).stream().map (o -> o.name())
            .collect(Collectors.toList());

        enum Field {
        /** 納入商品リレーション ID */
        Id,
        /** 納入商品リレーション番号 */
        AssetRelationshipNumber,
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
        /** 開始日 */
        FromDate,
        /** 終了日 */
        ToDate,
        /** リレーション種別 */
        RelationshipType
    }
}