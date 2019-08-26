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

/** ケース */
class SCase extends SearchLayout {

        String title = Field.Subject.name();
        List<String> contents = Arrays.asList(Field.CaseNumber, Field.SuppliedName, Field.SuppliedEmail,
        Field.SuppliedPhone, Field.SuppliedCompany, Field.Type, Field.Status, Field.Reason, Field.Origin,
        Field.Origin, Field.Subject, Field.Description, Field.ContactPhone, Field.ContactMobile,
                Field.ContactEmail, Field.ContactFax, Field.Comments)
        .stream().map(o -> o.name()).collect(Collectors.toList());

    enum Field {
        /** ケース ID */
        Id,
        /** ケース番号 */
        CaseNumber,
        /** 名前 */
        SuppliedName,
        /** メールアドレス */
        SuppliedEmail,
        /** 電話 */
        SuppliedPhone,
        /** 会社名 */
        SuppliedCompany,
        /** ケース種別 */
        Type,
        /** 状況 */
        Status,
        /** 原因 */
        Reason,
        /** 発生源 */
        Origin,
        /** 件名 */
        Subject,
        /** 優先度 */
        Priority,
        /** 説明 */
        Description,
        /** 完了日 */
        ClosedDate,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 取引先責任者 電話 */
        ContactPhone,
        /** 取引先責任者 携帯 */
        ContactMobile,
        /** 取引先責任者 メール */
        ContactEmail,
        /** 取引先責任者 Fax */
        ContactFax,
        /** 社内コメント */
        Comments,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate
    }
}
