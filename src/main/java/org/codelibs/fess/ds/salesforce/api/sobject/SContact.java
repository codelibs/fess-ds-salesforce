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

/** 取引先責任者 */
class SContact extends SearchLayout {
    String title = Field.Name.name();
    List<String> contents = Arrays.asList(Field.Name, Field.Phone, Field.Fax, Field.MobilePhone,
            Field.HomePhone, Field.OtherPhone, Field.Email, Field.Title, Field.Department,
            Field.Description)
            .stream().map(o -> o.name()).collect(Collectors.toList());

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

    enum Field {
        /** 取引先責任者 ID */
        Id,
        /** 姓 */
        LastName,
        /** 名 */
        FirstName,
        /** 敬称 */
        Salutation,
        /** 氏名 */
        Name,
        /** 町名・番地(その他) */
        OtherStreet,
        /** 市区郡(その他) */
        OtherCity,
        /** 都道府県(その他) */
        OtherState,
        /** 郵便番号(その他) */
        OtherPostalCode,
        /** 国(その他) */
        OtherCountry,
        /** Other Geocode Accuracy */
        OtherGeocodeAccuracy,
        /** 町名・番地(郵送先) */
        MailingStreet,
        /** 市区郡(郵送先) */
        MailingCity,
        /** 都道府県(郵送先) */
        MailingState,
        /** 郵便番号(郵送先) */
        MailingPostalCode,
        /** 国(郵送先) */
        MailingCountry,
        /** Mailing Geocode Accuracy */
        MailingGeocodeAccuracy,
        /** 電話 */
        Phone,
        /** Fax */
        Fax,
        /** 携帯電話 */
        MobilePhone,
        /** 自宅電話 */
        HomePhone,
        /** その他の電話 */
        OtherPhone,
        /** アシスタント電話 */
        AssistantPhone,
        /** メール */
        Email,
        /** 役職 */
        Title,
        /** 部署 */
        Department,
        /** アシスタント名 */
        AssistantName,
        /** リードソース */
        LeadSource,
        /** 誕生日 */
        Birthdate,
        /** 取引先責任者 説明 */
        Description,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 最終活動日 */
        LastActivityDate,
        /** 登録情報照会 最終依頼日 */
        LastCURequestDate,
        /** 登録情報照会 最終更新日 */
        LastCUUpdateDate,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** メール不達の理由 */
        EmailBouncedReason,
        /** メール不達発生日 */
        EmailBouncedDate,
        /** 写真の URL */
        PhotoUrl,
        /** Data.com キー */
        Jigsaw,
        /** Jigsaw Contact ID */
        JigsawContactId,
        /** 状況をクリーンアップ */
        CleanStatus
    }
}
