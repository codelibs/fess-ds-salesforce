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

/** 商品 */
public class SProduct2 extends SearchLayout {

    /** 商品 ID */
    protected static final String ID = "Id";
    /** 商品名 */
    protected static final String NAME = "Name";
    /** 商品コード */
    protected static final String PRODUCT_CODE = "ProductCode";
    /** 商品 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 商品ファミリ */
    protected static final String FAMILY = "Family";
    /** 外部 ID */
    protected static final String EXTERNAL_ID = "ExternalId";
    /** 表示 URL */
    protected static final String DISPLAY_URL = "DisplayUrl";
    /** 基準数量単位 */
    protected static final String QUANTITY_UNIT_OF_MEASURE = "QuantityUnitOfMeasure";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";
    /** 商品 SKU */
    protected static final String STOCK_KEEPING_UNIT = "StockKeepingUnit";

    protected static final List<String> contents = Arrays.asList(NAME, PRODUCT_CODE, DESCRIPTION, FAMILY, EXTERNAL_ID, DISPLAY_URL);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
