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
package org.codelibs.fess.ds.salesforce.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectorConfig;
import org.apache.log4j.Logger;
import org.codelibs.fess.ds.salesforce.SalesforceDataStore;
import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException;

public class BulkUtils {
    private static final Logger logger = Logger.getLogger(BulkUtils.class);

    public static BulkConnection getBulkConnection(final PartnerConnection connection) throws AsyncApiException {
        final ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(connection.getConfig().getSessionId());
        config.setRestEndpoint(connection.getConfig().getServiceEndpoint()
                .replaceFirst("/services.*", "/services/async/" + SalesforceDataStore.API_VERSION));
        return new BulkConnection(config);
    }

    public static JobInfo createJob(final BulkConnection connection, final String objectType) {
        try {
            final JobInfo job = new JobInfo();
            job.setObject(objectType);
            job.setOperation(OperationEnum.query);
            job.setConcurrencyMode(ConcurrencyMode.Parallel);
            job.setContentType(ContentType.JSON);
            return connection.createJob(job);
        } catch (final AsyncApiException e) {
            throw new SalesforceDataStoreException("Failed to create job.", e);
        }
    }

    public static BatchInfo createBatch(final BulkConnection connection, final JobInfo job, final String query) {
        try {
            return connection.createBatchFromStream(job, new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8)));
        } catch (final AsyncApiException e) {
            throw new SalesforceDataStoreException("Failed to create batch.", e);
        }
    }

    public static String createQuery(final String objectType, final List<String> fields) {
        return "SELECT " + String.join(",", fields) + " FROM " + objectType;
    }

    public static List<InputStream> getQueryResultStream(final BulkConnection connection,
                                                         final JobInfo job, final BatchInfo batch) {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        final CompletableFuture result = new CompletableFuture<String[]>();

        service.scheduleAtFixedRate(() -> {
            try {
                final BatchInfo info = connection.getBatchInfo(job.getId(), batch.getId(), ContentType.JSON);
                switch (info.getState()) {
                    case Completed: {
                        final QueryResultList queryResults = connection.getQueryResultList(job.getId(), batch.getId(), ContentType.JSON);
                        result.complete(queryResults.getResult());
                    }
                    case Failed: {
                        logger.warn("Batch:" + batch.getId() + " Failed caused by '" + info.getStateMessage() + "'");
                        result.complete(new String[0]);
                    }
                    default: {
                        logger.debug("Batch:" + batch.getId() + " " + info.getState());
                    }
                }
            } catch (final AsyncApiException e) {
                logger.warn(e);
                result.completeExceptionally(e);
            }
        }, 1, 10, TimeUnit.SECONDS);

        result.whenComplete((t, u) -> service.shutdownNow());

        try {
            return Arrays.stream((String[]) result.get())
                    .map(o -> {
                        try {
                            return connection.getQueryResultStream(job.getId(), batch.getId(), o);
                        } catch (final AsyncApiException e) {
                            throw new SalesforceDataStoreException("Failed to get query result stream by bulk connection.", e);
                        }
                    }
            ).collect(Collectors.toList());
        } catch (final Exception e) {
            throw new SalesforceDataStoreException("Failed to get query results.", e);
        }
    }
}
