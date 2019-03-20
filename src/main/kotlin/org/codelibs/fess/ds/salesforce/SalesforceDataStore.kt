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
package org.codelibs.fess.ds.salesforce

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sforce.async.BulkConnection
import com.sforce.soap.partner.PartnerConnection
import org.codelibs.fess.crawler.exception.CrawlingAccessException
import org.codelibs.fess.ds.AbstractDataStore
import org.codelibs.fess.ds.callback.IndexUpdateCallback
import org.codelibs.fess.ds.salesforce.api.*
import org.codelibs.fess.ds.salesforce.api.sobject.SObjects
import org.codelibs.fess.ds.salesforce.api.sobject.Searchable
import org.codelibs.fess.es.config.exentity.DataConfig
import org.codelibs.fess.util.ComponentUtil
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.full.primaryConstructor

class SalesforceDataStore : AbstractDataStore() {

    companion object {
        // parameters
        const val BASE_URL_PARAM = "base_url"
        const val AUTH_TYPE_PARAM = "auth_type"
        const val USERNAME_PARAM = "username"
        const val PASSWORD_PARAM = "password"
        const val SECURITY_TOKEN_PARAM = "security_token"
        const val CLIENT_ID_PARAM = "client_id"
        const val CLIENT_SECRET_PARAM = "client_secret"
        const val PRIVATE_KEY_PARAM = "private_key"

        // scripts
        const val SOBJECT = "sobject"
    }

    private val logger = LoggerFactory.getLogger(SalesforceDataStore::class.java)

    override fun getName(): String = "Salesforce"

    public override fun storeData(dataConfig: DataConfig, callback: IndexUpdateCallback, paramMap: Map<String, String>,
                                  scriptMap: Map<String, String>, defaultDataMap: Map<String, Any>) {
        val connection = try {
            getConnection(paramMap)
        } catch (e: SalesforceDataStoreException) {
            logger.error(e.message, e)
            return
        }

        val instanceUrl = connection.config.serviceEndpoint.replace("/services/.*".toRegex(), "")
        val bulk = getBulkConnection(connection)
        val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        storeSearchablesData(bulk, mapper, callback, paramMap, scriptMap, defaultDataMap, instanceUrl)
    }

    private fun storeSearchablesData(bulk: BulkConnection, mapper: ObjectMapper, callback: IndexUpdateCallback, paramMap: Map<String, String>, scriptMap: Map<String, String>, defaultDataMap: Map<String, Any>, instanceUrl: String) {
        SObjects.values().forEach { o ->
            val job = createJob(bulk, o.name)
            val fields = o.dataClass.primaryConstructor?.parameters?.mapNotNull { it.name } ?: ArrayList()
            val query = createQuery(o.name, fields)
            val batch = createBatch(bulk, job, query)
            getQueryResultStream(bulk, job, batch).forEach { stream ->
                mapper.readTree(stream).forEach { node ->
                    val obj = mapper.convertValue(node, o.dataClass.java)
                    storeSearchableData(callback, paramMap, scriptMap, defaultDataMap, obj, instanceUrl)
                }
            }
        }
    }

    private fun storeSearchableData(callback: IndexUpdateCallback, paramMap: Map<String, String>,
                                    scriptMap: Map<String, String>, defaultDataMap: Map<String, Any>, searchable: Searchable, instanceUrl: String) {
        val fessConfig = ComponentUtil.getFessConfig()
        val dataMap = HashMap(defaultDataMap)
        val resultMap = LinkedHashMap<String, Any>(paramMap)
        val objectMap = HashMap<String, Any?>()
        objectMap[fessConfig.indexFieldTitle] = searchable.title()
        objectMap[fessConfig.indexFieldContent] = searchable.content()
        objectMap[fessConfig.indexFieldDigest] = searchable.digest()
        objectMap[fessConfig.indexFieldCreated] = searchable.created()
        objectMap[fessConfig.indexFieldLastModified] = searchable.lastModified()
        objectMap[fessConfig.indexFieldUrl] = "$instanceUrl${searchable.urlPath()}"
        resultMap[SOBJECT] = objectMap
        try {
            for ((key, value) in scriptMap) {
                val convertValue = convertValue(value, resultMap)
                if (convertValue != null) {
                    dataMap[key] = convertValue
                }
            }
            callback.store(paramMap, dataMap)
        } catch (e: CrawlingAccessException) {
            logger.warn("Crawling Access Exception at : $dataMap", e)
        }
    }

    private fun getConnection(paramMap: Map<String, String>): PartnerConnection {
        val baseUrl = paramMap[BASE_URL_PARAM] ?: BASE_URL
        val authType = paramMap[AUTH_TYPE_PARAM]
        when (authType) {
            "oauth" -> {
                val username = paramMap[USERNAME_PARAM]
                val clientId = paramMap[CLIENT_ID_PARAM]
                val privateKey = paramMap[PRIVATE_KEY_PARAM]
                if (username == null || clientId == null || privateKey == null) {
                    throw SalesforceDataStoreException("parameters '$USERNAME_PARAM', '$CLIENT_ID_PARAM', '$PRIVATE_KEY_PARAM' are required for OAuth.")
                }
                return getConnection(username, clientId, privateKey, baseUrl)
            }
            "password" -> {
                val username = paramMap[USERNAME_PARAM]
                val password = paramMap[PASSWORD_PARAM]
                val securityToken = paramMap[SECURITY_TOKEN_PARAM]
                val clientId = paramMap[CLIENT_ID_PARAM]
                val clientSecret = paramMap[CLIENT_SECRET_PARAM]
                if (username == null || password == null || securityToken == null || clientId == null || clientSecret == null) {
                    throw SalesforceDataStoreException("parameters '$USERNAME_PARAM', '$PASSWORD_PARAM', '$SECURITY_TOKEN_PARAM', '$CLIENT_ID_PARAM', '$CLIENT_SECRET_PARAM' are required for Password Auth.")
                }
                return getConnectionByPassword(username, password, securityToken, clientId, clientSecret, baseUrl)
            }
            else -> {
                throw SalesforceDataStoreException("parameter '$AUTH_TYPE_PARAM' is required.")
            }
        }
    }

}
