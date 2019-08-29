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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codelibs.fess.ds.salesforce.api.SearchLayout;

/** リストメール */
public class SListEmail extends SearchLayout {

    protected static final String title = Field.Name.name();
    protected static final List<String> contents = Stream.of(Field.Name, Field.Subject, Field.TextBody,
            Field.FromName, Field.FromAddress, Field.Status).map(Enum::name).collect(Collectors.toList());

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

    private enum Field {
        /** リストメール ID */
        Id,
        /** 名前 */
        Name,
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
        /** 件名 */
        Subject,
        /** HTML 内容 */
        HtmlBody,
        /** テキスト内容 */
        TextBody,
        /** 差出人名 */
        FromName,
        /** 送信元アドレス */
        FromAddress,
        /** 状況 */
        Status,
        /** 予定日 */
        ScheduledDate
    }
}
