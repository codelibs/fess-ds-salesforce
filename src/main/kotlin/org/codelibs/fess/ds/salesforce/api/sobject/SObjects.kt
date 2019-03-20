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

import kotlin.reflect.KClass

enum class SObjects(val dataClass: KClass<out SObject>) {
    /** D&B 企業 */
    DandBCompany(SDandBCompany::class),
    /** ToDo */
    Task(STask::class),
    /** おすすめ */
    Recommendation(SRecommendation::class),
    /** アイデア */
    Idea(SIdea::class),
    /** キャンペーン */
    Campaign(SCampaign::class),
    /** クイックテキスト */
    QuickText(SQuickText::class),
    /** グループ */
    CollaborationGroup(SCollaborationGroup::class),
    /** ケース */
    Case(SCase::class),
    /** ソリューション */
    Solution(SSolution::class),
    /** マクロ */
    Macro(SMacro::class),
    /** ユーザ */
    User(SUser::class),
    /** リストメール */
    ListEmail(SListEmail::class),
    /** リード */
    Lead(SLead::class),
    /** 価格表 */
    Pricebook2(SPricebook2::class),
    /** 取引先 */
    Account(SAccount::class),
    /** 取引先責任者 */
    Contact(SContact::class),
    /** 商品 */
    Product2(SProduct2::class),
    /** 商談 */
    Opportunity(SOpportunity::class),
    /** 契約 */
    Contract(SContract::class),
    /** 注文 */
    Order(SOrder::class),
    /** 画像 */
    Image(SImage::class),
    /** 納入商品 */
    Asset(SAsset::class),
    /** 納入商品リレーション */
    AssetRelationship(SAssetRelationship::class)
}
