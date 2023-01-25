/*
 * Copyright 2012-2023 CodeLibs Project and the Others.
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

/** 画像 */
public class SImage extends SearchLayout {

    /** 画像 ID */
    protected static final String ID = "Id";
    /** 名前 */
    protected static final String NAME = "Name";
    /** 作成日 */
    protected static final String CREATED_DATE = "CreatedDate";
    /** 最終更新日 */
    protected static final String LAST_MODIFIED_DATE = "LastModifiedDate";
    /** System Modstamp */
    protected static final String SYSTEM_MODSTAMP = "SystemModstamp";
    /** 最終閲覧日 */
    protected static final String LAST_VIEWED_DATE = "LastViewedDate";
    /** 最終参照日 */
    protected static final String LAST_REFERENCED_DATE = "LastReferencedDate";
    /** 画像ビュー種別 */
    protected static final String IMAGE_VIEW_TYPE = "ImageViewType";
    /** 画像タイトル */
    protected static final String IMAGE_TITLE = "ImageTitle";
    /** 画像の代替テキスト */
    protected static final String IMAGE_ALTERNATE_TEXT = "ImageAlternateText";
    /** 画像の URL */
    protected static final String IMAGE_URL = "ImageUrl";
    /** 画像クラス */
    protected static final String IMAGE_CLASS = "ImageClass";
    /** 画像クラスオブジェクト種別 */
    protected static final String IMAGE_CLASS_OBJECT_TYPE = "ImageClassObjectType";
    /** キャプチャ角度 */
    protected static final String CAPTURED_ANGLE = "CapturedAngle";

    protected static final List<String> contents = Arrays.asList(NAME, IMAGE_TITLE, IMAGE_ALTERNATE_TEXT, IMAGE_URL);

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public List<String> getContents() {
        return contents;
    }

}
