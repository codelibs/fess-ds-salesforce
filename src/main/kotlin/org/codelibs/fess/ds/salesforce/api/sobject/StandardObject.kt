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

import org.codelibs.fess.ds.salesforce.api.SearchLayout

enum class StandardObject(val layout: SearchLayout) {
    /** D&B 企業 */
    DandBCompany(SDandBCompany()),
    /** ToDo */
    Task(STask()),
    /** おすすめ */
    Recommendation(SRecommendation()),
    /** アイデア */
    Idea(SIdea()),
    /** キャンペーン */
    Campaign(SCampaign()),
    /** クイックテキスト */
    QuickText(SQuickText()),
    /** グループ */
    CollaborationGroup(SCollaborationGroup()),
    /** ケース */
    Case(SCase()),
    /** ソリューション */
    Solution(SSolution()),
    /** マクロ */
    Macro(SMacro()),
    /** ユーザ */
    User(SUser()),
    /** リストメール */
    ListEmail(SListEmail()),
    /** リード */
    Lead(SLead()),
    /** 価格表 */
    Pricebook2(SPricebook2()),
    /** 取引先 */
    Account(SAccount()),
    /** 取引先責任者 */
    Contact(SContact()),
    /** 商品 */
    Product2(SProduct2()),
    /** 商談 */
    Opportunity(SOpportunity()),
    /** 契約 */
    Contract(SContract()),
    /** 注文 */
    Order(SOrder()),
    /** 画像 */
    Image(SImage()),
    /** 納入商品 */
    Asset(SAsset()),
    /** 納入商品リレーション */
    AssetRelationship(SAssetRelationship())
}
