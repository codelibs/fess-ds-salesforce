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
import org.codelibs.fess.ds.AbstractDataStore
import org.codelibs.fess.ds.callback.IndexUpdateCallback
import org.codelibs.fess.ds.salesforce.api.*
import org.codelibs.fess.ds.salesforce.api.sobject.StandardObject
import org.codelibs.fess.es.config.exentity.DataConfig
import org.codelibs.fess.util.ComponentUtil
import org.slf4j.LoggerFactory
import java.util.*

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

        const val TITLE_PARAM = "title"
        const val CONTENTS_PARAM = "contents"
        const val DIGESTS_PARAM = "digests"
        const val THUMBNAIL_PARAM = "thumbnail"
        const val CUSTOM_PARAM = "custom"
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

        val instanceUrl = connection.config.serviceEndpoint.replace(Regex("/services/.*"), "")
        val bulk = getBulkConnection(connection)
        val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        storeStandardObjects(bulk, mapper, callback, paramMap, scriptMap, defaultDataMap, instanceUrl)
        storeCustomObjects(bulk, mapper, callback, paramMap, scriptMap, defaultDataMap, instanceUrl)
    }

    private fun storeStandardObjects(bulk: BulkConnection, mapper: ObjectMapper,
                                     callback: IndexUpdateCallback, paramMap: Map<String, String>, scriptMap: Map<String, String>, defaultDataMap: Map<String, Any>,
                                     instanceUrl: String) {
        StandardObject.values().forEach { o ->
            val job = createJob(bulk, o.name)
            val layout = getSearchLayout(paramMap, o)
            val query = createQuery(o.name, layout.fields())
            val batch = createBatch(bulk, job, query)
            getQueryResultStream(bulk, job, batch).forEach { stream ->
                mapper.readTree(stream).forEach {
                    val data = SearchData.fromJson(o.name, it, layout)
                    storeSObjectData(callback, paramMap, scriptMap, defaultDataMap, data, instanceUrl)
                }
            }
        }
    }

    private fun storeCustomObjects(bulk: BulkConnection, mapper: ObjectMapper,
                                   callback: IndexUpdateCallback, paramMap: Map<String, String>, scriptMap: Map<String, String>, defaultDataMap: Map<String, Any>,
                                   instanceUrl: String) {
        getCustomObjects(paramMap).forEach { c ->
            val job = createJob(bulk, c)
            val layout = getSearchLayout(paramMap, c)
            val query = createQuery(c, layout.fields())
            val batch = createBatch(bulk, job, query)
            getQueryResultStream(bulk, job, batch).forEach { stream ->
                mapper.readTree(stream).forEach {
                    val data = SearchData.fromJson(c, it, layout)
                    storeSObjectData(callback, paramMap, scriptMap, defaultDataMap, data, instanceUrl)
                }
            }
        }
    }

    private fun storeSObjectData(callback: IndexUpdateCallback, paramMap: Map<String, String>, scriptMap: Map<String, String>, defaultDataMap: Map<String, Any>,
                                 data: SearchData, instanceUrl: String) {
        val fessConfig = ComponentUtil.getFessConfig()
        val dataMap = HashMap(defaultDataMap)
        dataMap[fessConfig.indexFieldTitle] = "[${data.type}] ${data.title}"
        dataMap[fessConfig.indexFieldContent] = data.content
        dataMap[fessConfig.indexFieldContentLength] = data.content.length
        dataMap[fessConfig.indexFieldDigest] = data.digest
        dataMap[fessConfig.indexFieldCreated] = data.created
        dataMap[fessConfig.indexFieldLastModified] = data.lastModified
        dataMap[fessConfig.indexFieldUrl] = "$instanceUrl/${data.id}"
        dataMap[fessConfig.indexFieldThumbnail] = data.thumbnail
        callback.store(paramMap, dataMap)
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

    private fun getSearchLayout(paramMap: Map<String, String>, obj: StandardObject): SearchLayout = object : SearchLayout {
        override val title: String =
                paramMap["${obj.name}.$TITLE_PARAM"]
                        ?: obj.layout.title
        override val contents: List<String> =
                paramMap["${obj.name}.$CONTENTS_PARAM"]?.split(",")?.map { it.trim() }
                        ?: obj.layout.contents
        override val digests: List<String> =
                paramMap["${obj.name}.$DIGESTS_PARAM"]?.split(",")?.map { it.trim() }
                        ?: obj.layout.digests
        override val thumbnail: String? =
                paramMap["${obj.name}.$THUMBNAIL_PARAM"]
                        ?: obj.layout.thumbnail
    }

    private fun getSearchLayout(paramMap: Map<String, String>, type: String): SearchLayout = object : SearchLayout {
        override val title: String =
                paramMap["$type.$TITLE_PARAM"] ?: type
        override val contents: List<String> =
                paramMap["$type.$CONTENTS_PARAM"]?.split(",")?.map { it.trim() } ?: emptyList()
        override val digests: List<String> =
                paramMap["$type.$DIGESTS_PARAM"]?.split(",")?.map { it.trim() } ?: emptyList()
        override val thumbnail: String? =
                paramMap["$type.$THUMBNAIL_PARAM"]
    }

    private fun getCustomObjects(paramMap: Map<String, String>): List<String> =
            paramMap[CUSTOM_PARAM]?.split(",")?.map { it.trim() } ?: emptyList()

}
