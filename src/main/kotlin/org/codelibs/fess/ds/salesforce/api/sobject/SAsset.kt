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
package org.codelibs.fess.ds.salesforce.api.sobject

import org.codelibs.fess.ds.salesforce.api.SearchLayout
import org.codelibs.fess.ds.salesforce.api.sobject.SAsset.Field.*

/** 納入商品 */
data class SAsset(
        override val title: String = Name.name,
        override val contents: List<String> = listOf(Name, SerialNumber, Status, Description).map { it.name }
) : SearchLayout {
    enum class Field {
        /** 納入商品 ID */
        Id,
        /** 商品コード */
        ProductCode,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 納入商品名 */
        Name,
        /** シリアル番号 */
        SerialNumber,
        /** 納入日 */
        InstallDate,
        /** 購入日 */
        PurchaseDate,
        /** 使用終了日 */
        UsageEndDate,
        /** 状況 */
        Status,
        /** 説明 */
        Description,
        /** 商品 SKU */
        StockKeepingUnit,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate
    }
}
