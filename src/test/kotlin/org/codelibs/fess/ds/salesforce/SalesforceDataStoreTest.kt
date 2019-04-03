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

import org.codelibs.fess.ds.callback.IndexUpdateCallback
import org.codelibs.fess.ds.salesforce.api.AuthenticationTest.Companion.BASE_URL
import org.codelibs.fess.ds.salesforce.api.AuthenticationTest.Companion.CLIENT_ID
import org.codelibs.fess.ds.salesforce.api.AuthenticationTest.Companion.PRIVATE_KEY
import org.codelibs.fess.ds.salesforce.api.AuthenticationTest.Companion.USERNAME
import org.codelibs.fess.es.config.exentity.DataConfig
import org.codelibs.fess.util.ComponentUtil
import org.dbflute.utflute.lastadi.ContainerTestCase
import org.slf4j.LoggerFactory

class SalesforceDataStoreTest : ContainerTestCase() {

    private val logger = LoggerFactory.getLogger(SalesforceDataStoreTest::class.java)

    private lateinit var dataStore: SalesforceDataStore

    override fun prepareConfigFile(): String = "test_app.xml"
    override fun isSuppressTestCaseTransaction(): Boolean = true

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        dataStore = SalesforceDataStore()
    }

    @Throws(Exception::class)
    override fun tearDown() {
        ComponentUtil.setFessConfig(null)
        super.tearDown()
    }

    fun testStoreData() {
        // doStoreData()
    }

    private fun doStoreData() {
        val dataConfig = DataConfig()
        val paramMap = mapOf(
                "auth_type" to "oauth",
                "username" to USERNAME,
                "client_id" to CLIENT_ID,
                "private_key" to PRIVATE_KEY,
                "base_url" to BASE_URL
        )
        val fessConfig = ComponentUtil.getFessConfig()
        val scriptMap = emptyMap<String, String>()
        val defaultDataMap = emptyMap<String, Any>()
        dataStore.storeData(dataConfig, testCallback { dataMap: Map<String, Any> ->
            logger.debug(dataMap[fessConfig.indexFieldTitle].toString())
        }, paramMap, scriptMap, defaultDataMap)
    }

}

private fun testCallback(action: (Map<String, Any>) -> Unit): IndexUpdateCallback = object : IndexUpdateCallback {
    private var documentSize: Long = 0L
    private var executeTime: Long = 0L

    override fun store(paramMap: Map<String, String>, dataMap: Map<String, Any>) {
        val startTime = System.currentTimeMillis()
        action(dataMap)
        executeTime += System.currentTimeMillis() - startTime
        documentSize++
    }

    override fun getDocumentSize(): Long = documentSize
    override fun getExecuteTime(): Long = executeTime
    override fun commit() {}
}
