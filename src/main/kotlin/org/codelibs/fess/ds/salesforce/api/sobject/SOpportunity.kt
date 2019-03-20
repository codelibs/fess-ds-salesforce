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

/** 商談 */
data class SOpportunity(
        /** 商談 ID */
        override val id: String,
        /** 商談名 */
        val name: String,
        /** 説明 */
        val description: String?,
        /** フェーズ */
        val stageName: String,
        /** 完了予定日 */
        val closeDate: Date,
        /** 商談 種別 */
        val type: String?,
        /** 次のステップ */
        val nextStep: String?,
        /** リードソース */
        val leadSource: String?,
        /** 売上予測分類 */
        val forecastCategory: String,
        /** 売上予測分類 */
        val forecastCategoryName: String?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 最終活動日 */
        val lastActivityDate: Date?,
        /** 会計期間 */
        val fiscal: String?,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?
) : SObject {
    override fun title(): String = "${super.title()}"
    override fun content(): String = listOfNotNull("").joinToString("\n")
    override val objectType: SObjects = SObjects.Opportunity
}
