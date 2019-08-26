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

public enum StandardObject {

    /** D&B 企業 */
    DandBCompany(new SDandBCompany()),
    /** ToDo */
    Task(new STask()),
    /** おすすめ */
    Recommendation(new SRecommendation()),
    /** アイデア */
    Idea(new SIdea()),
    /** キャンペーン */
    Campaign(new SCampaign()),
    /** クイックテキスト */
    QuickText(new SQuickText()),
    /** グループ */
    CollaborationGroup(new SCollaborationGroup()),
    /** ケース */
    Case(new SCase()),
    /** ソリューション */
    Solution(new SSolution()),
    /** マクロ */
    Macro(new SMacro()),
    /** ユーザ */
    User(new SUser()),
    /** リストメール */
    ListEmail(new SListEmail()),
    /** リード */
    Lead(new SLead()),
    /** 価格表 */
    Pricebook2(new SPricebook2()),
    /** 取引先 */
    Account(new SAccount()),
    /** 取引先責任者 */
    Contact(new SContact()),
    /** 商品 */
    Product2(new SProduct2()),
    /** 商談 */
    Opportunity(new SOpportunity()),
    /** 契約 */
    Contract(new SContract()),
    /** 注文 */
    Order(new SOrder()),
    /** 画像 */
    Image(new SImage()),
    /** 納入商品 */
    Asset(new SAsset()),
    /** 納入商品リレーション */
    AssetRelationship(new SAssetRelationship());

    private final SearchLayout layout;

    public final SearchLayout getLayout() {
        return this.layout;
    }

    StandardObject(SearchLayout layout) {
        this.layout = layout;
    }
}