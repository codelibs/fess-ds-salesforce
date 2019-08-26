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

/** ToDo */
class STask extends SearchLayout {

    String title = Field.Subject.name();
    List<String> contents = Arrays.asList(Field.Subject, Field.Status, Field.Priority,
            Field.Description)
            .stream().map(o -> o.name()).collect(Collectors.toList());

    enum Field {
        /** 活動 ID */
        Id,
        /** 件名 */
        Subject,
        /** 期日のみ */
        ActivityDate,
        /** 状況 */
        Status,
        /** 優先度 */
        Priority,
        /** 説明 */
        Description,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 通話種別 */
        CallType,
        /** 通話結果 */
        CallDisposition,
        /** 通話オブジェクト ID */
        CallObject,
        /** アラーム日付/時間 */
        ReminderDateTime,
        /** 繰り返しの開始 */
        RecurrenceStartDateOnly,
        /** 繰り返しの終了 */
        RecurrenceEndDateOnly,
        /** 繰り返しタイムゾーン */
        RecurrenceTimeZoneSidKey,
        /** 繰り返し種別 */
        RecurrenceType,
        /** 繰り返しインスタンス */
        RecurrenceInstance,
        /** 繰り返し月 */
        RecurrenceMonthOfYear,
        /** この ToDo を繰り返す */
        RecurrenceRegeneratedType,
        /** ToDo のサブ種別 */
        TaskSubtype,
        /** 完了日 */
        CompletedDateTime
    }
}
