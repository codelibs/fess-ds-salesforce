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

import java.util.Arrays;
import java.util.List;

import org.codelibs.fess.ds.salesforce.api.SearchLayout;

/** 納入商品 */
public class SAsset extends SearchLayout {

    /** 納入商品 ID */
    protected static final String ID = "Id";
    /** 商品コード */
    protected static final String PRODUCT_CODE = "ProductCode";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 納入商品名 */
    protected static final String NAME = "Name";
    /** シリアル番号 */
    protected static final String SERIAL_NUMBER = "SerialNumber";
    /** 納入日 */
    protected static final String INSTALL_DATE = "InstallDate";
    /** 購入日 */
    protected static final String PURCHASE_DATE = "PurchaseDate";
    /** 使用終了日 */
    protected static final String USAGE_END_DATE = "UsageEndDate";
    /** 状況 */
    protected static final String STATUS = "Status";
    /** 説明 */
    protected static final String DESCRIPTION = "Description";
    /** 商品 SKU */
    protected static final String STOCK_KEEPING_UNIT = "StockKeepingUnit";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";

    protected static final List<String> contents = Arrays.asList(NAME, SERIAL_NUMBER, STATUS, DESCRIPTION);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
