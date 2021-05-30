/*
 * Copyright 2012-2021 CodeLibs Project and the Others.
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

import java.util.Arrays;
import java.util.List;

import org.codelibs.fess.ds.salesforce.api.SearchLayout;

/** ToDo */
public class STask extends SearchLayout {

    /** 活動 ID */
    protected static final String ID = "Id";
    /** 件名 */
    protected static final String SUBJECT = "Subject";
    /** 期日のみ */
    protected static final String ACTIVITY_DATE = "ActivityDate";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 優先度 */
    protected static final String PRIORITY = "Priority";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 通話種別 */
    protected static final String CALL_TYPE = "CallType";
    /** 通話結果 */
    protected static final String CALL_DISPOSITION = "CallDisposition";
    /** 通話オブジェクト ID */
    protected static final String CALL_OBJECT = "CallObject";
    /** アラーム日付/時間 */
    protected static final String REMINDER_DATE_TIME = "ReminderDateTime";
    /** 繰り返しの開始 */
    protected static final String RECURRENCE_START_DATE_ONLY = "RecurrenceStartDateOnly";
    /** 繰り返しの終了 */
    protected static final String RECURRENCE_END_DATE_ONLY = "RecurrenceEndDateOnly";
    /** 繰り返しタイムゾーン */
    protected static final String RECURRENCE_TIME_ZONE_SID_KEY = "RecurrenceTimeZoneSidKey";
    /** 繰り返し種別 */
    protected static final String RECURRENCE_TYPE = "RecurrenceType";
    /** 繰り返しインスタンス */
    protected static final String RECURRENCE_INSTANCE = "RecurrenceInstance";
    /** 繰り返し月 */
    protected static final String RECURRENCE_MONTH_OF_YEAR = "RecurrenceMonthOfYear";
    /** この ToDo を繰り返す */
    protected static final String RECURRENCE_REGENERATED_TYPE = "RecurrenceRegeneratedType";
    /** ToDo のサブ種別 */
    protected static final String TASK_SUBTYPE = "TaskSubtype";
    /** 完了日 */
    protected static final String COMPLETED_DATE_TIME = "CompletedDateTime";

    protected static final List<String> contents = Arrays.asList(SUBJECT, STATUS, PRIORITY, DESCRIPTION);

    @Override
    public String getTitle() {
        return SUBJECT;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
