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
package org.codelibs.fess.ds.salesforce.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BulkConnection;
import com.sforce.async.ConcurrencyMode;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.OperationEnum;
import com.sforce.async.QueryResultList;
import org.apache.log4j.Logger;
import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException;

public class BulkUtil {
    private static final Logger logger = Logger.getLogger(BulkUtil.class);

    public static JobInfo createJob(final BulkConnection connection, final String objectType) {
        try {
            final JobInfo job = new JobInfo();
            job.setObject(objectType);
            job.setOperation(OperationEnum.query);
            job.setConcurrencyMode(ConcurrencyMode.Parallel);
            job.setContentType(ContentType.JSON);
            final JobInfo result = connection.createJob(job);
            if (logger.isDebugEnabled()) {
                logger.info("Created a job : " + job);
            }
            return result;
        } catch (final AsyncApiException e) {
            throw new SalesforceDataStoreException("Failed to create job.", e);
        }
    }

    public static BatchInfo createBatch(final BulkConnection connection, final JobInfo job, final String query) {
        try {
            final BatchInfo batch = connection.createBatchFromStream(job, new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8)));
            if (logger.isDebugEnabled()) {
                logger.info("Created a batch : " + batch);
            }
            return batch;
        } catch (final AsyncApiException e) {
            throw new SalesforceDataStoreException("Failed to create batch.", e);
        }
    }

    public static String createQuery(final String objectType, final List<String> fields) {
        return "SELECT " + String.join(",", fields) + " FROM " + objectType;
    }

    public static List<InputStream> getQueryResultStream(final BulkConnection connection, final JobInfo job,
                                                         final BatchInfo batch, final boolean ignoreError) {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        final CompletableFuture future = new CompletableFuture<String[]>();

        executor.scheduleAtFixedRate(() -> {
            try {
                final BatchInfo info = connection.getBatchInfo(job.getId(), batch.getId(), ContentType.JSON);
                switch (info.getState()) {
                    case Completed: {
                        final QueryResultList queryResults = connection.getQueryResultList(job.getId(), batch.getId(), ContentType.JSON);
                        future.complete(queryResults.getResult());
                        break;
                    }
                    case Failed: {
                        logger.warn("Batch:" + batch.getId() + " Failed caused by '" + info.getStateMessage() + "'");
                        future.complete(new String[0]);
                        break;
                    }
                    default: {
                        logger.debug("Batch:" + batch.getId() + " " + info.getState());
                    }
                }
            } catch (final AsyncApiException e) {
                logger.warn(e);
                future.completeExceptionally(e);
            }
        }, 1, 10, TimeUnit.SECONDS);

        future.whenComplete((result, exceptions) -> executor.shutdownNow());

        try {
            return Arrays.stream((String[]) future.get())
                    .map(o -> {
                        try {
                            return connection.getQueryResultStream(job.getId(), batch.getId(), o);
                        } catch (final AsyncApiException e) {
                            throw new SalesforceDataStoreException("Failed to get query result stream by bulk connection.", e);
                        }
                    }
            ).collect(Collectors.toList());
        } catch (final InterruptedException | ExecutionException e) {
            if (ignoreError) {
                logger.warn("Failed to get query results. JOB = " + job + ", BATCH = " + batch, e);
                return Collections.emptyList();
            } else {
                throw new SalesforceDataStoreException("Failed to get query results.", e);
            }
        }

    }

}
