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

/** グループ */
data class SCollaborationGroup(
        /** グループ ID */
        override val id: String,
        /** 名前 */
        val name: String,
        /** アクセス種別 */
        val collaborationType: String,
        /** 説明 */
        val description: String?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 実寸大の写真の URL */
        val fullPhotoUrl: String?,
        /** プロファイル写真 (中) の URL */
        val mediumPhotoUrl: String?,
        /** 写真 */
        val smallPhotoUrl: String?,
        /** 最終フィード更新日 */
        val lastFeedModifiedDate: Date,
        /** 情報のタイトル */
        val informationTitle: String?,
        /** 情報 */
        val informationBody: String?,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?,
        /** グループのメール */
        val groupEmail: String?,
        /** バナー写真の URL */
        val bannerPhotoUrl: String?
) : SObject {
    override fun title(): String = "${super.title()} $name"
    override fun content(): String = listOfNotNull(id, name, description, informationTitle, informationBody, groupEmail).joinToString("\n")
    override fun thumbnail(): String? = smallPhotoUrl
    override val objectType: SObjects = SObjects.CollaborationGroup
}
