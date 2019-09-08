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
package org.codelibs.fess.ds.salesforce;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.codelibs.core.lang.StringUtil;
import org.codelibs.fess.Constants;
import org.codelibs.fess.app.service.FailureUrlService;
import org.codelibs.fess.crawler.exception.CrawlingAccessException;
import org.codelibs.fess.crawler.exception.MultipleCrawlingAccessException;
import org.codelibs.fess.crawler.filter.UrlFilter;
import org.codelibs.fess.ds.AbstractDataStore;
import org.codelibs.fess.ds.callback.IndexUpdateCallback;
import org.codelibs.fess.ds.salesforce.api.SearchData;
import org.codelibs.fess.es.config.exentity.DataConfig;
import org.codelibs.fess.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesforceDataStore extends AbstractDataStore {

    private static final Logger logger = LoggerFactory.getLogger(SalesforceDataStore.class);

    // parameters
    protected static final String IGNORE_ERROR = "ignore_error";
    protected static final String INCLUDE_PATTERN = "include_pattern";
    protected static final String EXCLUDE_PATTERN = "exclude_pattern";
    protected static final String URL_FILTER = "url_filter";
    protected static final String NUMBER_OF_THREADS = "number_of_threads";

    // scripts
    protected static final String OBJECT = "object";
    protected static final String OBJECT_ID = "id";
    protected static final String OBJECT_TYPE = "type";
    protected static final String OBJECT_TITLE = "title";
    protected static final String OBJECT_CONTENT = "content";
    protected static final String OBJECT_DESCRIPTION = "description";
    protected static final String OBJECT_CONTENT_LENGTH = "content_length";
    protected static final String OBJECT_URL = "url";
    protected static final String OBJECT_CREATED = "created";
    protected static final String OBJECT_LAST_MODIFIED = "last_modified";
    protected static final String OBJECT_THUMBNAIL = "thumbnail";

    @Override
    public String getName() {
        return "Salesforce";
    }

    @Override
    public void storeData(final DataConfig dataConfig, final IndexUpdateCallback callback, final Map<String, String> paramMap,
                          final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap) {

        final Map<String, Object> configMap = new HashMap<>();
        configMap.put(IGNORE_ERROR, isIgnoreError(paramMap));
        configMap.put(URL_FILTER, getUrlFilter(paramMap));
        if (logger.isDebugEnabled()) {
            logger.debug("configMap: {}", configMap);
        }

        final SalesforceClient client = createClient(paramMap);

        final ExecutorService executorService = newFixedThreadPool(Integer.parseInt(paramMap.getOrDefault(NUMBER_OF_THREADS, "1")));
        try {
            storeStandardObjects(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, executorService, client);
            storeCustomObjects(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, executorService, client);
            if (logger.isDebugEnabled()) {
                logger.debug("Shutting down thread executor.");
            }
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Interrupted.", e);
            }
        } finally {
            executorService.shutdownNow();
        }
    }

    protected SalesforceClient createClient(final Map<String, String> paramMap) {
        return new SalesforceClient(paramMap);
    }

    protected boolean isIgnoreError(final Map<String, String> paramMap) {
        return paramMap.getOrDefault(IGNORE_ERROR, Constants.TRUE).equalsIgnoreCase(Constants.TRUE);
    }

    protected UrlFilter getUrlFilter(final Map<String, String> paramMap) {
        final UrlFilter urlFilter = ComponentUtil.getComponent(UrlFilter.class);
        final String include = paramMap.get(INCLUDE_PATTERN);
        if (StringUtil.isNotBlank(include)) {
            urlFilter.addInclude(include);
        }
        final String exclude = paramMap.get(EXCLUDE_PATTERN);
        if (StringUtil.isNotBlank(exclude)) {
            urlFilter.addExclude(exclude);
        }
        urlFilter.init(paramMap.get(Constants.CRAWLING_INFO_ID));
        if (logger.isDebugEnabled()) {
            logger.debug("urlFilter: {}", urlFilter);
        }
        return urlFilter;
    }

    protected ExecutorService newFixedThreadPool(final int nThreads) {
        if (logger.isDebugEnabled()) {
            logger.debug("Executor Thread Pool: {}", nThreads);
        }
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(nThreads),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    protected void storeStandardObjects(final DataConfig dataConfig, final IndexUpdateCallback callback, final Map<String, Object> configMap,
                                      final Map<String, String> paramMap, final Map<String, String> scriptMap,
                                      final Map<String, Object> defaultDataMap, final ExecutorService executorService,
                                      final SalesforceClient client) throws InterruptedException {
        final boolean ignoreError = (Boolean) configMap.get(IGNORE_ERROR);
        client.getStandardObjects(data ->
                executorService.execute(() ->
                        processSearchData(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, data, client)
                ), ignoreError);
    }

    protected void storeCustomObjects(final DataConfig dataConfig, final IndexUpdateCallback callback, final Map<String, Object> configMap,
                                    final Map<String, String> paramMap, final Map<String, String> scriptMap,
                                    final Map<String, Object> defaultDataMap, final ExecutorService executorService,
                                    final SalesforceClient client) throws InterruptedException {
        final boolean ignoreError = (Boolean) configMap.get(IGNORE_ERROR);
        client.getCustomObjects(data ->
                executorService.execute(() ->
                        processSearchData(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, data, client)
                ),
                ignoreError);
    }

    protected void processSearchData(final DataConfig dataConfig, final IndexUpdateCallback callback,
                                   final Map<String, Object> configMap, final Map<String, String> paramMap,
                                   final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap,
                                   final SearchData data, final SalesforceClient client) {
        final Map<String, Object> dataMap = new HashMap<>(defaultDataMap);
        final String url = client.getInstanceUrl() + "/" + data.getId();
        try {

            final UrlFilter urlFilter = (UrlFilter) configMap.get(URL_FILTER);
            if (urlFilter != null && !urlFilter.match(url)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Not matched: {}", url);
                }
                return;
            }

            logger.info("Crawling URL: {}", url);

            final Map<String, Object> resultMap = new LinkedHashMap<>(paramMap);
            final Map<String, Object> objectMap = new HashMap<>();

            objectMap.put(OBJECT_ID, data.getId());
            objectMap.put(OBJECT_TYPE, data.getType());
            objectMap.put(OBJECT_TITLE, data.getTitle());
            objectMap.put(OBJECT_CONTENT, data.getContent());
            objectMap.put(OBJECT_DESCRIPTION, data.getDescription());
            objectMap.put(OBJECT_CONTENT_LENGTH, data.getContent().length());
            objectMap.put(OBJECT_CREATED, data.getCreated());
            objectMap.put(OBJECT_LAST_MODIFIED, data.getLastModified());
            objectMap.put(OBJECT_URL, url);
            objectMap.put(OBJECT_THUMBNAIL, data.getThumbnail());
            resultMap.put(OBJECT, objectMap);

            if (logger.isDebugEnabled()) {
                logger.debug("resultMap: {}", resultMap);
            }

            for (final Map.Entry<String, String> entry : scriptMap.entrySet()) {
                final Object convertValue = convertValue(entry.getValue(), resultMap);
                if (convertValue != null) {
                    dataMap.put(entry.getKey(), convertValue);
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("dataMap: {}", dataMap);
            }

            callback.store(paramMap, dataMap);
        } catch (final CrawlingAccessException e) {
            logger.warn("Crawling Access Exception at : " + dataMap, e);

            Throwable target = e;
            if (target instanceof MultipleCrawlingAccessException) {
                final Throwable[] causes = ((MultipleCrawlingAccessException) target).getCauses();
                if (causes.length > 0) {
                    target = causes[causes.length - 1];
                }
            }

            String errorName;
            final Throwable cause = target.getCause();
            if (cause != null) {
                errorName = cause.getClass().getCanonicalName();
            } else {
                errorName = target.getClass().getCanonicalName();
            }

            final FailureUrlService failureUrlService = ComponentUtil.getComponent(FailureUrlService.class);
            failureUrlService.store(dataConfig, errorName, url, target);
        } catch (final Throwable t) {
            logger.warn("Crawling Access Exception at : " + dataMap, t);
            final FailureUrlService failureUrlService = ComponentUtil.getComponent(FailureUrlService.class);
            failureUrlService.store(dataConfig, t.getClass().getCanonicalName(), url, t);
        }
    }

}
