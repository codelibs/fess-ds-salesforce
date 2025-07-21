/*
 * Copyright 2012-2025 CodeLibs Project and the Others.
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

/**
 * Represents a Salesforce User object for system users and their profile information.
 * This class extends SearchLayout to provide search functionality for users
 * including their names, contact information, roles, and organizational details.
 */
public class SUser extends SearchLayout {

    /**
     * Creates a new instance.
     */
    public SUser() {
    }

    /** ユーザ ID */
    protected static final String ID = "Id";
    /** ユーザ名 */
    protected static final String USERNAME = "Username";
    /** 姓 */
    protected static final String LAST_NAME = "LastName";
    /** 名 */
    protected static final String FIRST_NAME = "FirstName";
    /** 氏名 */
    protected static final String NAME = "Name";
    /** 会社名 */
    protected static final String COMPANY_NAME = "CompanyName";
    /** ディビジョン */
    protected static final String DIVISION = "Division";
    /** 部署 */
    protected static final String DEPARTMENT = "Department";
    /** 役職 */
    protected static final String TITLE = "Title";
    /** 町名・番地 */
    protected static final String STREET = "Street";
    /** 市区郡 */
    protected static final String CITY = "City";
    /** 都道府県 */
    protected static final String STATE = "State";
    /** 郵便番号 */
    protected static final String POSTAL_CODE = "PostalCode";
    /** 国 */
    protected static final String COUNTRY = "Country";
    /** Geocode Accuracy */
    protected static final String GEOCODE_ACCURACY = "GeocodeAccuracy";
    /** メール */
    protected static final String EMAIL = "Email";
    /** 送信者のメールアドレス */
    protected static final String SENDER_EMAIL = "SenderEmail";
    /** メール送信者の名前 */
    protected static final String SENDER_NAME = "SenderName";
    /** メールの署名 */
    protected static final String SIGNATURE = "Signature";
    /** 登録情報照会メールの件名 */
    protected static final String STAY_IN_TOUCH_SUBJECT = "StayInTouchSubject";
    /** 登録情報照会メールの署名 */
    protected static final String STAY_IN_TOUCH_SIGNATURE = "StayInTouchSignature";
    /** 登録情報照会メールのメモ */
    protected static final String STAY_IN_TOUCH_NOTE = "StayInTouchNote";
    /** 電話 */
    protected static final String PHONE = "Phone";
    /** Fax */
    protected static final String FAX = "Fax";
    /** モバイル */
    protected static final String MOBILE_PHONE = "MobilePhone";
    /** 別名 */
    protected static final String ALIAS = "Alias";
    /** ニックネーム */
    protected static final String COMMUNITY_NICKNAME = "CommunityNickname";
    /** ユーザの写真のバッジテキストのフロート表示 */
    protected static final String BADGE_TEXT = "BadgeText";
    /** タイムゾーン */
    protected static final String TIME_ZONE_SID_KEY = "TimeZoneSidKey";
    /** 地域 */
    protected static final String LOCALE_SID_KEY = "LocaleSidKey";
    /** メールの文字コード */
    protected static final String EMAIL_ENCODING_KEY = "EmailEncodingKey";
    /** ユーザ種別 */
    protected static final String USER_TYPE = "UserType";
    /** 言語 */
    protected static final String LANGUAGE_LOCALE_KEY = "LanguageLocaleKey";
    /** 従業員番号 */
    protected static final String EMPLOYEE_NUMBER = "EmployeeNumber";
    /** 最終ログイン */
    protected static final String LAST_LOGIN_DATE = "LastLoginDate";
    /** 前回のパスワードの変更またはリセット */
    protected static final String LAST_PASS_CHANGE_DATE = "LastPasswordChangeDate";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** Force.com Connect Offline トライアル期限 */
    protected static final String OFFLINE_TRIAL_EXPIRATION_DATE = "OfflineTrialExpirationDate";
    /** Sales Anywhere トライアル期限 */
    protected static final String OFFLINE_PDA_TRIAL_EXPIRATION_DATE = "OfflinePdaTrialExpirationDate";
    /** 内線 */
    protected static final String EXTENSION = "Extension";
    /** SAML 統合 ID */
    protected static final String FEDERATION_IDENTIFIER = "FederationIdentifier";
    /** 自己紹介 */
    protected static final String ABOUT_ME = "AboutMe";
    /** 実寸大の写真の URL */
    protected static final String FULL_PHOTO_URL = "FullPhotoUrl";
    /** 写真 */
    protected static final String SMALL_PHOTO_URL = "SmallPhotoUrl";
    /** 不在通知 */
    protected static final String OUT_OF_OFFICE_MESSAGE = "OutOfOfficeMessage";
    /** プロファイル写真 (中) の URL */
    protected static final String MEDIUM_PHOTO_URL = "MediumPhotoUrl";
    /** Chatter メールハイライト送信頻度 */
    protected static final String DIGEST_FREQUENCY = "DigestFrequency";
    /** グループに参加する場合のデフォルト通知頻度 */
    protected static final String DEFAULT_GROUP_NOTIFICATION_FREQUENCY = "DefaultGroupNotificationFrequency";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";
    /** バナー写真の URL */
    protected static final String BANNER_PHOTO_URL = "BannerPhotoUrl";
    /** iOS バナー写真の URL */
    protected static final String SMALL_BANNER_PHOTO_URL = "SmallBannerPhotoUrl";
    /** Android バナー写真の URL */
    protected static final String MEDIUM_BANNER_PHOTO_URL = "MediumBannerPhotoUrl";

    /** List of default fields used for content indexing in search results. */
    protected static final List<String> contents = Arrays.asList(USERNAME, NAME, COMPANY_NAME, DIVISION, DEPARTMENT, TITLE, EMAIL, PHONE,
            FAX, MOBILE_PHONE, ALIAS, COMMUNITY_NICKNAME, ABOUT_ME);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
