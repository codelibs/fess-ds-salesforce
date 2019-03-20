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

/** ToDo */
data class STask(
        /** 活動 ID */
        override val id: String,
        /** 件名 */
        val subject: String?,
        /** 期日のみ */
        val activityDate: Date?,
        /** 状況 */
        val status: String,
        /** 優先度 */
        val priority: String,
        /** 説明 */
        val description: String?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** 通話種別 */
        val callType: String?,
        /** 通話結果 */
        val callDisposition: String?,
        /** 通話オブジェクト ID */
        val callObject: String?,
        /** アラーム日付/時間 */
        val reminderDateTime: Date?,
        /** 繰り返しの開始 */
        val recurrenceStartDateOnly: Date?,
        /** 繰り返しの終了 */
        val recurrenceEndDateOnly: Date?,
        /** 繰り返しタイムゾーン */
        val recurrenceTimeZoneSidKey: String?,
        /** 繰り返し種別 */
        val recurrenceType: String?,
        /** 繰り返しインスタンス */
        val recurrenceInstance: String?,
        /** 繰り返し月 */
        val recurrenceMonthOfYear: String?,
        /** この ToDo を繰り返す */
        val recurrenceRegeneratedType: String?,
        /** ToDo のサブ種別 */
        val taskSubtype: String?,
        /** 完了日 */
        val completedDateTime: Date?
) : SObject {
    override fun title(): String = "${super.title()}"
    override fun content(): String = listOfNotNull("").joinToString("\n")
    override val objectType: SObjects = SObjects.Task
}
