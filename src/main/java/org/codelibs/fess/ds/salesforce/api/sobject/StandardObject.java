/*
 * Copyright 2012-2022 CodeLibs Project and the Others.
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

    /** D&amp;B 企業 */
    DAND_B_COMPANY(new SDandBCompany()),
    /** ToDo */
    TASK(new STask()),
    /** おすすめ */
    RECOMMENDATION(new SRecommendation()),
    /** アイデア */
    IDEA(new SIdea()),
    /** キャンペーン */
    CAMPAIGN(new SCampaign()),
    /** クイックテキスト */
    QUICK_TEXT(new SQuickText()),
    /** グループ */
    COLLABORATION_GROUP(new SCollaborationGroup()),
    /** ケース */
    CASE(new SCase()),
    /** ソリューション */
    SOLUTION(new SSolution()),
    /** マクロ */
    MACRO(new SMacro()),
    /** ユーザ */
    USER(new SUser()),
    /** リストメール */
    LIST_EMAIL(new SListEmail()),
    /** リード */
    LEAD(new SLead()),
    /** 価格表 */
    PRICEBOOK2(new SPricebook2()),
    /** 取引先 */
    ACCOUNT(new SAccount()),
    /** 取引先責任者 */
    CONTACT(new SContact()),
    /** 商品 */
    PRODUCT2(new SProduct2()),
    /** 商談 */
    OPPORTUNITY(new SOpportunity()),
    /** 契約 */
    CONTRACT(new SContract()),
    /** 注文 */
    ORDER(new SOrder()),
    /** 画像 */
    IMAGE(new SImage()),
    /** 納入商品 */
    ASSET(new SAsset()),
    /** 納入商品リレーション */
    ASSET_RELATIONSHIP(new SAssetRelationship());

    private final SearchLayout layout;

    public final SearchLayout getLayout() {
        return this.layout;
    }

    StandardObject(final SearchLayout layout) {
        this.layout = layout;
    }
}