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

/** リード */
class SLead extends SearchLayout {

    String title = Field.Name.name();
    List<String> contents = Arrays.asList(Field.Name, Field.Title, Field.Company,
            Field.Phone, Field.MobilePhone, Field.Fax, Field.Email, Field.Website, Field.Description,
            Field.Status, Field.Industry)
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
        /** リード ID */
        Id,
        /** 姓 */
        LastName,
        /** 名 */
        FirstName,
        /** 敬称 */
        Salutation,
        /** 氏名 */
        Name,
        /** 役職 */
        Title,
        /** 会社名 */
        Company,
        /** 町名・番地 */
        Street,
        /** 市区郡 */
        City,
        /** 都道府県 */
        State,
        /** 郵便番号 */
        PostalCode,
        /** 国 */
        Country,
        /** Geocode Accuracy */
        GeocodeAccuracy,
        /** 電話 */
        Phone,
        /** 携帯電話 */
        MobilePhone,
        /** Fax */
        Fax,
        /** メール */
        Email,
        /** Web サイト */
        Website,
        /** 写真の URL */
        PhotoUrl,
        /** 説明 */
        Description,
        /** リードソース */
        LeadSource,
        /** 状況 */
        Status,
        /** 業種 */
        Industry,
        /** 評価 */
        Rating,
        /** 取引開始日 */
        ConvertedDate,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** 最終活動日 */
        LastActivityDate,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** Data.com キー */
        Jigsaw,
        /** Jigsaw Contact ID */
        JigsawContactId,
        /** 状況をクリーンアップ */
        CleanStatus,
        /** 会社 D-U-N-S 番号 */
        CompanyDunsNumber,
        /** メール不達の理由 */
        EmailBouncedReason,
        /** メール不達発生日 */
        EmailBouncedDate
    }
}
