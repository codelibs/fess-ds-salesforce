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

/** アイデア */
data class SIdea(
        /** アイデア ID */
        override val id: String,
        /** タイトル */
        val title: String,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?,
        /** アイデア本文 */
        val body: String?,
        /** カテゴリ */
        val categories: String?,
        /** 状況 */
        val status: String?,
        /** 最終アイデアのコメント日 */
        val lastCommentDate: Date?,
        /** 作成者のプロファイル写真の URL */
        val creatorFullPhotoUrl: String?,
        /** 作成者の写真のサムネイルの URL */
        val creatorSmallPhotoUrl: String?,
        /** 作成者の名前 */
        val creatorName: String?
) : SObject {
    override fun title(): String = "${super.title()} $title"
    override fun content(): String = listOfNotNull(id, title, body, categories, status, creatorName).joinToString("\n")
    override val objectType: SObjects = SObjects.Idea
}
