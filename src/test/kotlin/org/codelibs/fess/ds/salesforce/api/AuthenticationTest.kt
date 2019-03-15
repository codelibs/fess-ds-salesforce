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

import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException
import org.dbflute.utflute.lastadi.ContainerTestCase
import org.slf4j.LoggerFactory

class AuthenticationTest : ContainerTestCase() {

    companion object {
        const val BASE_URL = ""
        const val USERNAME = ""
        const val PASSWORD = ""
        const val SECURITY_TOKEN = ""
        const val CLIENT_ID = ""
        const val CLIENT_SECRET = ""
        const val PRIVATE_KEY = ""
    }

    private val logger = LoggerFactory.getLogger(AuthenticationTest::class.java)

    override fun prepareConfigFile(): String = "test_app.xml"
    override fun isSuppressTestCaseTransaction(): Boolean = true

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    fun testGetTokenResponse() {
        // doGetTokenResponse()
        // doGetTokenResponseByPassword()
    }

    private fun doGetTokenResponse() {
        try {
            val response = getTokenResponse(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL)
            logger.debug("AccessToken: ${response.accessToken}")
        } catch (e: SalesforceDataStoreException) {
            fail("Failed to get AccessToken by '${e.message}'")
        }
    }

    private fun doGetTokenResponseByPassword() {
        try {
            val response = getTokenResponseByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL)
            logger.debug("AccessToken: ${response.accessToken}")
        } catch (e: SalesforceDataStoreException) {
            fail("Failed to get AccessToken by '${e.message}'")
        }
    }

    fun testGetConnection() {
        // doGetConnection()
        // doGetConnectionByPassword()
    }

    private fun doGetConnection() {
        getConnection(USERNAME, CLIENT_ID, PRIVATE_KEY, BASE_URL)
    }

    private fun doGetConnectionByPassword() {
        getConnectionByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, BASE_URL)
    }

}