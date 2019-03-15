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
package org.codelibs.fess.ds.salesforce.api

import com.sforce.async.*
import com.sforce.async.BatchStateEnum.Completed
import com.sforce.async.BatchStateEnum.Failed
import org.apache.log4j.Logger
import java.io.InputStream
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Bulks

private val logger = Logger.getLogger(Bulks::class.java)

fun createJob(connection: BulkConnection, objectType: String): JobInfo {
    val job = JobInfo()
    job.`object` = objectType
    job.operation = OperationEnum.query
    job.concurrencyMode = ConcurrencyMode.Parallel
    job.contentType = ContentType.JSON
    return connection.createJob(job)
}

fun createBatch(connection: BulkConnection, job: JobInfo, query: String): BatchInfo =
        connection.createBatchFromStream(job, query.byteInputStream(Charsets.UTF_8))

fun createQuery(objectType: String, fields: List<String>): String =
        "SELECT ${fields.joinToString(",")} FROM $objectType"

fun getQueryResultStream(connection: BulkConnection, job: JobInfo, batch: BatchInfo): List<InputStream> {
    val service = Executors.newSingleThreadScheduledExecutor()
    val result = CompletableFuture<Array<String>>()

    service.scheduleAtFixedRate({
        try {
            val info = connection.getBatchInfo(job.id, batch.id, ContentType.JSON)
            when (info.state) {
                Completed -> {
                    val queryResults = connection.getQueryResultList(job.id, batch.id, ContentType.JSON)
                    result.complete(queryResults.result)
                }
                Failed -> {
                    logger.debug("Batch:${batch.id} Failed")
                    result.complete(arrayOf())
                }
                else -> {
                    logger.debug("Batch:${batch.id} ${info.state}")
                }
            }
        } catch (e: AsyncApiException) {
            logger.warn(e)
            result.completeExceptionally(e)
        }
    }, 1, 10, TimeUnit.SECONDS)

    result.whenComplete { _, _ -> service.shutdownNow() }

    return result.get().map { connection.getQueryResultStream(job.id, batch.id, it) }
}
