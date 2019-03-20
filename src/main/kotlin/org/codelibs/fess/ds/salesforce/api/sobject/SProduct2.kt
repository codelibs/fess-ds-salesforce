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

import java.util.*

/** 商品 */
data class SProduct2(
        /** 商品 ID */
        override val id: String,
        /** 商品名 */
        val name: String,
        /** 商品コード */
        val productCode: String?,
        /** 商品 説明 */
        val description: String?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 商品ファミリ */
        val family: String?,
        /** 外部 ID */
        val externalId: String?,
        /** 表示 URL */
        val displayUrl: String?,
        /** 基準数量単位 */
        val quantityUnitOfMeasure: String?,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?,
        /** 商品 SKU */
        val stockKeepingUnit: String?
) : SObject {
    override fun title(): String = "${super.title()} $name"
    override fun content(): String = listOfNotNull(id, name, productCode, description, family, externalId, displayUrl).joinToString("\n")
    override val objectType: SObjects = SObjects.Product2
}
