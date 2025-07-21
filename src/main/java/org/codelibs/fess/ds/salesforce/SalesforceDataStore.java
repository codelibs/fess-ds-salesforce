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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codelibs.core.exception.InterruptedRuntimeException;
import org.codelibs.core.lang.StringUtil;
import org.codelibs.fess.Constants;
import org.codelibs.fess.app.service.FailureUrlService;
import org.codelibs.fess.crawler.exception.CrawlingAccessException;
import org.codelibs.fess.crawler.exception.MultipleCrawlingAccessException;
import org.codelibs.fess.crawler.filter.UrlFilter;
import org.codelibs.fess.ds.AbstractDataStore;
import org.codelibs.fess.ds.callback.IndexUpdateCallback;
import org.codelibs.fess.ds.salesforce.api.SearchData;
import org.codelibs.fess.entity.DataStoreParams;
import org.codelibs.fess.helper.CrawlerStatsHelper;
import org.codelibs.fess.helper.CrawlerStatsHelper.StatsAction;
import org.codelibs.fess.helper.CrawlerStatsHelper.StatsKeyObject;
import org.codelibs.fess.opensearch.config.exentity.DataConfig;
import org.codelibs.fess.util.ComponentUtil;

/**
 * DataStore implementation for integrating Salesforce data with Fess search engine.
 * This class handles crawling and indexing of Salesforce objects including both
 * standard objects (Account, Case, User, etc.) and custom objects.
 *
 * <p>The data store supports various authentication methods (OAuth token, username/password)
 * and provides parallel processing capabilities for efficient data retrieval from Salesforce APIs.</p>
 */
public class SalesforceDataStore extends AbstractDataStore {

    /**
     * Creates a new instance.
     */
    public SalesforceDataStore() {
    }

    private static final Logger logger = LogManager.getLogger(SalesforceDataStore.class);

    // parameters
    /** Parameter key for ignoring errors. */
    protected static final String IGNORE_ERROR = "ignore_error";
    /** Parameter key for include patterns. */
    protected static final String INCLUDE_PATTERN = "include_pattern";
    /** Parameter key for exclude patterns. */
    protected static final String EXCLUDE_PATTERN = "exclude_pattern";
    /** Parameter key for URL filter. */
    protected static final String URL_FILTER = "url_filter";
    /** Parameter key for the number of threads. */
    protected static final String NUMBER_OF_THREADS = "number_of_threads";

    // scripts
    /** Script variable for the SObject data. */
    protected static final String OBJECT = "object";
    /** Script variable for the SObject ID. */
    protected static final String OBJECT_ID = "id";
    /** Script variable for the SObject type. */
    protected static final String OBJECT_TYPE = "type";
    /** Script variable for the SObject title. */
    protected static final String OBJECT_TITLE = "title";
    /** Script variable for the SObject content. */
    protected static final String OBJECT_CONTENT = "content";
    /** Script variable for the SObject description. */
    protected static final String OBJECT_DESCRIPTION = "description";
    /** Script variable for the SObject content length. */
    protected static final String OBJECT_CONTENT_LENGTH = "content_length";
    /** Script variable for the SObject URL. */
    protected static final String OBJECT_URL = "url";
    /** Script variable for the SObject created date. */
    protected static final String OBJECT_CREATED = "created";
    /** Script variable for the SObject last modified date. */
    protected static final String OBJECT_LAST_MODIFIED = "last_modified";
    /** Script variable for the SObject thumbnail. */
    protected static final String OBJECT_THUMBNAIL = "thumbnail";

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void storeData(final DataConfig dataConfig, final IndexUpdateCallback callback, final DataStoreParams paramMap,
            final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap) {

        final Map<String, Object> configMap = new HashMap<>();
        configMap.put(IGNORE_ERROR, isIgnoreError(paramMap));
        configMap.put(URL_FILTER, getUrlFilter(paramMap));
        if (logger.isDebugEnabled()) {
            logger.debug("configMap: {}", configMap);
        }

        final ExecutorService executorService = newFixedThreadPool(Integer.parseInt(paramMap.getAsString(NUMBER_OF_THREADS, "1")));
        try (final SalesforceClient client = createClient(paramMap)) {
            storeStandardObjects(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, executorService, client);
            storeCustomObjects(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, executorService, client);
            if (logger.isDebugEnabled()) {
                logger.debug("Shutting down thread executor.");
            }
            executorService.shutdown();
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            throw new InterruptedRuntimeException(e);
        } finally {
            executorService.shutdownNow();
        }
    }

    /**
     * Creates a SalesforceClient.
     *
     * @param paramMap The data store parameters.
     * @return A SalesforceClient instance.
     */
    protected SalesforceClient createClient(final DataStoreParams paramMap) {
        return new SalesforceClient(paramMap);
    }

    /**
     * Checks if errors should be ignored.
     *
     * @param paramMap The data store parameters.
     * @return true if errors should be ignored, false otherwise.
     */
    protected boolean isIgnoreError(final DataStoreParams paramMap) {
        return Constants.TRUE.equalsIgnoreCase(paramMap.getAsString(IGNORE_ERROR, Constants.TRUE));
    }

    /**
     * Gets the URL filter.
     *
     * @param paramMap The data store parameters.
     * @return The URL filter.
     */
    protected UrlFilter getUrlFilter(final DataStoreParams paramMap) {
        final UrlFilter urlFilter = ComponentUtil.getComponent(UrlFilter.class);
        final String include = paramMap.getAsString(INCLUDE_PATTERN);
        if (StringUtil.isNotBlank(include)) {
            urlFilter.addInclude(include);
        }
        final String exclude = paramMap.getAsString(EXCLUDE_PATTERN);
        if (StringUtil.isNotBlank(exclude)) {
            urlFilter.addExclude(exclude);
        }
        urlFilter.init(paramMap.getAsString(Constants.CRAWLING_INFO_ID));
        if (logger.isDebugEnabled()) {
            logger.debug("urlFilter: {}", urlFilter);
        }
        return urlFilter;
    }

    /**
     * Creates a new fixed thread pool.
     *
     * @param nThreads The number of threads.
     * @return A new ExecutorService.
     */
    protected ExecutorService newFixedThreadPool(final int nThreads) {
        if (logger.isDebugEnabled()) {
            logger.debug("Executor Thread Pool: {}", nThreads);
        }
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(nThreads),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * Stores standard objects.
     *
     * @param dataConfig The data config.
     * @param callback The index update callback.
     * @param configMap The config map.
     * @param paramMap The data store parameters.
     * @param scriptMap The script map.
     * @param defaultDataMap The default data map.
     * @param executorService The executor service.
     * @param client The Salesforce client.
     */
    protected void storeStandardObjects(final DataConfig dataConfig, final IndexUpdateCallback callback,
            final Map<String, Object> configMap, final DataStoreParams paramMap, final Map<String, String> scriptMap,
            final Map<String, Object> defaultDataMap, final ExecutorService executorService, final SalesforceClient client) {
        final boolean ignoreError = (Boolean) configMap.get(IGNORE_ERROR);
        client.getStandardObjects(
                data -> executorService.execute(
                        () -> processSearchData(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, data, client)),
                ignoreError);
    }

    /**
     * Stores custom objects.
     *
     * @param dataConfig The data config.
     * @param callback The index update callback.
     * @param configMap The config map.
     * @param paramMap The data store parameters.
     * @param scriptMap The script map.
     * @param defaultDataMap The default data map.
     * @param executorService The executor service.
     * @param client The Salesforce client.
     */
    protected void storeCustomObjects(final DataConfig dataConfig, final IndexUpdateCallback callback, final Map<String, Object> configMap,
            final DataStoreParams paramMap, final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap,
            final ExecutorService executorService, final SalesforceClient client) {
        final boolean ignoreError = (Boolean) configMap.get(IGNORE_ERROR);
        client.getCustomObjects(
                data -> executorService.execute(
                        () -> processSearchData(dataConfig, callback, configMap, paramMap, scriptMap, defaultDataMap, data, client)),
                ignoreError);
    }

    /**
     * Processes the search data.
     *
     * @param dataConfig The data config.
     * @param callback The index update callback.
     * @param configMap The config map.
     * @param paramMap The data store parameters.
     * @param scriptMap The script map.
     * @param defaultDataMap The default data map.
     * @param data The search data.
     * @param client The Salesforce client.
     */
    protected void processSearchData(final DataConfig dataConfig, final IndexUpdateCallback callback, final Map<String, Object> configMap,
            final DataStoreParams paramMap, final Map<String, String> scriptMap, final Map<String, Object> defaultDataMap,
            final SearchData data, final SalesforceClient client) {
        final CrawlerStatsHelper crawlerStatsHelper = ComponentUtil.getCrawlerStatsHelper();
        final Map<String, Object> dataMap = new HashMap<>(defaultDataMap);
        final String url = client.getInstanceUrl() + "/" + data.getId();
        final StatsKeyObject statsKey = new StatsKeyObject(url);
        paramMap.put(Constants.CRAWLER_STATS_KEY, statsKey);
        try {
            crawlerStatsHelper.begin(statsKey);

            final UrlFilter urlFilter = (UrlFilter) configMap.get(URL_FILTER);
            if (urlFilter != null && !urlFilter.match(url)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Not matched: {}", url);
                }
                crawlerStatsHelper.discard(statsKey);
                return;
            }

            logger.info("Crawling URL: {}", url);

            final Map<String, Object> resultMap = new LinkedHashMap<>(paramMap.asMap());
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

            crawlerStatsHelper.record(statsKey, StatsAction.PREPARED);

            if (logger.isDebugEnabled()) {
                logger.debug("resultMap: {}", resultMap);
            }

            final String scriptType = getScriptType(paramMap);
            for (final Map.Entry<String, String> entry : scriptMap.entrySet()) {
                final Object convertValue = convertValue(scriptType, entry.getValue(), resultMap);
                if (convertValue != null) {
                    dataMap.put(entry.getKey(), convertValue);
                }
            }

            crawlerStatsHelper.record(statsKey, StatsAction.EVALUATED);

            if (logger.isDebugEnabled()) {
                logger.debug("dataMap: {}", dataMap);
            }

            if (dataMap.get("url") instanceof final String statsUrl) {
                statsKey.setUrl(statsUrl);
            }

            callback.store(paramMap, dataMap);
            crawlerStatsHelper.record(statsKey, StatsAction.FINISHED);
        } catch (final CrawlingAccessException e) {
            logger.warn("Crawling Access Exception at : {}", dataMap, e);

            Throwable target = e;
            if (target instanceof final MultipleCrawlingAccessException ex) {
                final Throwable[] causes = ex.getCauses();
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
            crawlerStatsHelper.record(statsKey, StatsAction.ACCESS_EXCEPTION);
        } catch (final Throwable t) {
            logger.warn("Crawling Access Exception at : {}", dataMap, t);
            final FailureUrlService failureUrlService = ComponentUtil.getComponent(FailureUrlService.class);
            failureUrlService.store(dataConfig, t.getClass().getCanonicalName(), url, t);
            crawlerStatsHelper.record(statsKey, StatsAction.EXCEPTION);
        } finally {
            crawlerStatsHelper.done(statsKey);
        }
    }

}