package org.codelibs.fess.ds.salesforce

import org.dbflute.utflute.lastadi.ContainerTestCase
import org.slf4j.LoggerFactory

class ForceHelperTest : ContainerTestCase() {

    companion object {
        const val HOST = ""
        const val USERNAME = ""
        const val PASSWORD = ""
        const val SECURITY_TOKEN = ""
        const val CLIENT_ID = ""
        const val CLIENT_SECRET = ""
        const val PRIVATE_KEY = ""
    }

    private val logger = LoggerFactory.getLogger(ForceHelperTest::class.java)

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
            val response = ForceHelper.getTokenResponse(USERNAME, CLIENT_ID, PRIVATE_KEY, HOST)
            logger.debug("AccessToken: ${response.accessToken}")
        } catch (e: SalesforceDataStoreException) {
            fail("Failed to get AccessToken by '${e.message}'")
        }
    }

    private fun doGetTokenResponseByPassword() {
        try {
            val response = ForceHelper.getTokenResponseByPassword(USERNAME, PASSWORD, SECURITY_TOKEN, CLIENT_ID, CLIENT_SECRET, HOST)
            logger.debug("AccessToken: ${response.accessToken}")
        } catch (e: SalesforceDataStoreException) {
            fail("Failed to get AccessToken by '${e.message}'")
        }
    }

    fun testGetBulkConnection() {
        // doGetBulkConnection()
    }

    private fun doGetBulkConnection() {
        ForceHelper.getBulkConnection(USERNAME, CLIENT_ID, PRIVATE_KEY, HOST)
    }
}