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

/** ユーザ */
data class SUser(
        /** ユーザ ID */
        override val id: String,
        /** ユーザ名 */
        val username: String,
        /** 姓 */
        val lastName: String,
        /** 名 */
        val firstName: String?,
        /** 氏名 */
        val name: String,
        /** 会社名 */
        val companyName: String?,
        /** ディビジョン */
        val division: String?,
        /** 部署 */
        val department: String?,
        /** 役職 */
        val title: String?,
        /** 町名・番地 */
        val street: String?,
        /** 市区郡 */
        val city: String?,
        /** 都道府県 */
        val state: String?,
        /** 郵便番号 */
        val postalCode: String?,
        /** 国 */
        val country: String?,
        /** Geocode Accuracy */
        val geocodeAccuracy: String?,
        /** メール */
        val email: String,
        /** 送信者のメールアドレス */
        val senderEmail: String?,
        /** メール送信者の名前 */
        val senderName: String?,
        /** メールの署名 */
        val signature: String?,
        /** 登録情報照会メールの件名 */
        val stayInTouchSubject: String?,
        /** 登録情報照会メールの署名 */
        val stayInTouchSignature: String?,
        /** 登録情報照会メールのメモ */
        val stayInTouchNote: String?,
        /** 電話 */
        val phone: String?,
        /** Fax */
        val fax: String?,
        /** モバイル */
        val mobilePhone: String?,
        /** 別名 */
        val alias: String,
        /** ニックネーム */
        val communityNickname: String,
        /** ユーザの写真のバッジテキストのフロート表示 */
        val badgeText: String?,
        /** タイムゾーン */
        val timeZoneSidKey: String,
        /** 地域 */
        val localeSidKey: String,
        /** メールの文字コード */
        val emailEncodingKey: String,
        /** ユーザ種別 */
        val userType: String?,
        /** 言語 */
        val languageLocaleKey: String,
        /** 従業員番号 */
        val employeeNumber: String?,
        /** 最終ログイン */
        val lastLoginDate: Date?,
        /** 前回のパスワードの変更またはリセット */
        val lastPasswordChangeDate: Date?,
        /** 作成日 */
        override val createdDate: Date,
        /** 最終更新日 */
        override val lastModifiedDate: Date,
        /** System Modstamp */
        val systemModstamp: Date,
        /** Force.com Connect Offline トライアル期限 */
        val offlineTrialExpirationDate: Date?,
        /** Sales Anywhere トライアル期限 */
        val offlinePdaTrialExpirationDate: Date?,
        /** 内線 */
        val extension: String?,
        /** SAML 統合 ID */
        val federationIdentifier: String?,
        /** 自己紹介 */
        val aboutMe: String?,
        /** 実寸大の写真の URL */
        val fullPhotoUrl: String?,
        /** 写真 */
        val smallPhotoUrl: String?,
        /** 不在通知 */
        val outOfOfficeMessage: String?,
        /** プロファイル写真 (中) の URL */
        val mediumPhotoUrl: String?,
        /** Chatter メールハイライト送信頻度 */
        val digestFrequency: String,
        /** グループに参加する場合のデフォルト通知頻度 */
        val defaultGroupNotificationFrequency: String,
        /** 最終閲覧日 */
        val lastViewedDate: Date?,
        /** 最終参照日 */
        val lastReferencedDate: Date?,
        /** バナー写真の URL */
        val bannerPhotoUrl: String?,
        /** iOS バナー写真の URL */
        val smallBannerPhotoUrl: String?,
        /** Android バナー写真の URL */
        val mediumBannerPhotoUrl: String?
) : SObject {
    override fun title(): String = "${super.title()} $name"
    override fun content(): String = listOfNotNull(id, username, name, companyName, division, department, title, email, phone, fax, mobilePhone, alias, communityNickname, aboutMe).joinToString("\n")
    override fun thumbnail(): String? = smallPhotoUrl
    override val objectType: SObjects = SObjects.User
}
