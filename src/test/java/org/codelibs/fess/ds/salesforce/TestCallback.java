package org.codelibs.fess.ds.salesforce;

import org.codelibs.fess.ds.callback.IndexUpdateCallback;

import java.util.Map;
import java.util.function.Consumer;

public class TestCallback implements IndexUpdateCallback {

    protected Consumer<Map<String, Object>> action;
    private Long documentSize = 0L;
    private Long executeTime = 0L;

    public TestCallback(Consumer<Map<String, Object>> action) {
        this.action = action;
    }

    @Override
    public void store(Map<String, String> paramMap, Map<String, Object> dataMap) {
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
