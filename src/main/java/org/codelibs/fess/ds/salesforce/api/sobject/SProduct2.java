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

/** 商品 */
class SProduct2 extends SearchLayout {

    String title = Field.Name.name();
    List<String> contents = Arrays.asList(Field.Name, Field.ProductCode, Field.Description,
            Field.Family, Field.ExternalId, Field.DisplayUrl)
            .stream().map(o -> o.name()).collect(Collectors.toList());

    enum Field {
        /** 商品 ID */
        Id,
        /** 商品名 */
        Name,
        /** 商品コード */
        ProductCode,
        /** 商品 説明 */
        Description,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 商品ファミリ */
        Family,
        /** 外部 ID */
        ExternalId,
        /** 表示 URL */
        DisplayUrl,
        /** 基準数量単位 */
        QuantityUnitOfMeasure,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** 商品 SKU */
        StockKeepingUnit
    }
}