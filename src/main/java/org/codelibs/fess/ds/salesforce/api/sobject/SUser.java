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

/** ユーザ */
class SUser extends SearchLayout {

    String title = Field.Name.name();
    List<String> contents = Arrays.asList(Field.Username, Field.Name, Field.CompanyName,
            Field.Division, Field.Department, Field.Title, Field.Email, Field.Phone, Field.Fax,
            Field.MobilePhone, Field.Alias, Field.CommunityNickname, Field.AboutMe)
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
        /** ユーザ ID */
        Id,
        /** ユーザ名 */
        Username,
        /** 姓 */
        LastName,
        /** 名 */
        FirstName,
        /** 氏名 */
        Name,
        /** 会社名 */
        CompanyName,
        /** ディビジョン */
        Division,
        /** 部署 */
        Department,
        /** 役職 */
        Title,
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
        /** メール */
        Email,
        /** 送信者のメールアドレス */
        SenderEmail,
        /** メール送信者の名前 */
        SenderName,
        /** メールの署名 */
        Signature,
        /** 登録情報照会メールの件名 */
        StayInTouchSubject,
        /** 登録情報照会メールの署名 */
        StayInTouchSignature,
        /** 登録情報照会メールのメモ */
        StayInTouchNote,
        /** 電話 */
        Phone,
        /** Fax */
        Fax,
        /** モバイル */
        MobilePhone,
        /** 別名 */
        Alias,
        /** ニックネーム */
        CommunityNickname,
        /** ユーザの写真のバッジテキストのフロート表示 */
        BadgeText,
        /** タイムゾーン */
        TimeZoneSidKey,
        /** 地域 */
        LocaleSidKey,
        /** メールの文字コード */
        EmailEncodingKey,
        /** ユーザ種別 */
        UserType,
        /** 言語 */
        LanguageLocaleKey,
        /** 従業員番号 */
        EmployeeNumber,
        /** 最終ログイン */
        LastLoginDate,
        /** 前回のパスワードの変更またはリセット */
        LastPasswordChangeDate,
        /** 作成日 */
        CreatedDate,
        /** 最終更新日 */
        LastModifiedDate,
        /** System Modstamp */
        SystemModstamp,
        /** Force.com Connect Offline トライアル期限 */
        OfflineTrialExpirationDate,
        /** Sales Anywhere トライアル期限 */
        OfflinePdaTrialExpirationDate,
        /** 内線 */
        Extension,
        /** SAML 統合 ID */
        FederationIdentifier,
        /** 自己紹介 */
        AboutMe,
        /** 実寸大の写真の URL */
        FullPhotoUrl,
        /** 写真 */
        SmallPhotoUrl,
        /** 不在通知 */
        OutOfOfficeMessage,
        /** プロファイル写真 (中) の URL */
        MediumPhotoUrl,
        /** Chatter メールハイライト送信頻度 */
        DigestFrequency,
        /** グループに参加する場合のデフォルト通知頻度 */
        DefaultGroupNotificationFrequency,
        /** 最終閲覧日 */
        LastViewedDate,
        /** 最終参照日 */
        LastReferencedDate,
        /** バナー写真の URL */
        BannerPhotoUrl,
        /** iOS バナー写真の URL */
        SmallBannerPhotoUrl,
        /** Android バナー写真の URL */
        MediumBannerPhotoUrl
    }
}
