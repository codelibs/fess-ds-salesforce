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
package org.codelibs.fess.ds.salesforce.api;

import com.sforce.async.*;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sforce.async.BatchStateEnum.Completed;

public class Bulks {
    private Logger logger = Logger.getLogger(Bulks.class);

    public JobInfo createJob(BulkConnection connection, String objectType) throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setObject(objectType);
        job.setOperation(OperationEnum.query);
        job.setConcurrencyMode(ConcurrencyMode.Parallel);
        job.setContentType(ContentType.JSON);
        return connection.createJob(job);
    }

    public BatchInfo createBatch(BulkConnection connection, JobInfo job, String query) throws AsyncApiException {
        return connection.createBatchFromStream(job, new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8)));
    }

    public String createQuery(String objectType, List<String> fields) {
        return "SELECT " + String.join(",", fields) + " FROM " + objectType;
    }

    private List<InputStream> getQueryResultStream(BulkConnection connection,
                                                   JobInfo job, BatchInfo batch) throws Exception {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        CompletableFuture result = new CompletableFuture<String[]>();

        service.scheduleAtFixedRate(() -> {
            try {
                BatchInfo info = connection.getBatchInfo(job.getId(), batch.getId(), ContentType.JSON);
                switch (info.getState()) {
                    case Completed: {
                        QueryResultList queryResults = connection.getQueryResultList(job.getId(), batch.getId(), ContentType.JSON);
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
            } catch (AsyncApiException e) {
                logger.warn(e);
                result.completeExceptionally(e);
            }
        }, 1, 10, TimeUnit.SECONDS);

        result.whenComplete((t, u) -> service.shutdownNow());

        return Arrays.asList((String[]) result.get()).stream().map(o -> {
                    try {
                        return connection.getQueryResultStream(job.getId(), batch.getId(), o);
                    } catch (AsyncApiException e) {
                        e.printStackTrace(); // TODO
                        return null;
                    }
                }
            ).collect(Collectors.toList());
    }
}
