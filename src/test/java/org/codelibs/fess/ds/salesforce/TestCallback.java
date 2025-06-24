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
package org.codelibs.fess.ds.salesforce;

import java.util.Map;
import java.util.function.Consumer;

import org.codelibs.fess.ds.callback.IndexUpdateCallback;
import org.codelibs.fess.entity.DataStoreParams;

public class TestCallback implements IndexUpdateCallback {

    protected Consumer<Map<String, Object>> action;
    private Long documentSize = 0L;
    private Long executeTime = 0L;

    public TestCallback(Consumer<Map<String, Object>> action) {
        this.action = action;
    }

    @Override
    public void store(DataStoreParams paramMap, Map<String, Object> dataMap) {
        long startTime = System.currentTimeMillis();
        action.accept(dataMap);
        executeTime += System.currentTimeMillis() - startTime;
        documentSize++;
    }

    @Override
    public long getDocumentSize() {
        return documentSize;
    }

    @Override
    public long getExecuteTime() {
        return executeTime;
    }

    @Override
    public void commit() {
    }
}
