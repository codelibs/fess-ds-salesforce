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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sforce.async.BulkConnection
import com.sforce.soap.partner.Connector
import com.sforce.soap.partner.PartnerConnection
import com.sforce.ws.ConnectorConfig
import org.apache.commons.codec.binary.Base64
import org.codelibs.curl.Curl
import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException
import java.io.InputStream
import java.security.KeyFactory
import java.security.Signature
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

internal const val BASE_URL = "https://login.salesforce.com"
internal const val API_VERSION = "45.0"

fun getConnection(username: String, clientId: String, privateKeyPem: String, baseUrl: String = BASE_URL): PartnerConnection {
    val response = getTokenResponse(username, clientId, privateKeyPem, baseUrl)
    val config = createConnectorConfig(response)
    return Connector.newConnection(config)
}

fun getConnectionByPassword(username: String, password: String, securityToken: String, clientId: String, clientSecret: String, baseUrl: String = BASE_URL): PartnerConnection {
    val response = getTokenResponseByPassword(username, password, securityToken, clientId, clientSecret, baseUrl)
    val config = createConnectorConfig(response)
    return Connector.newConnection(config)
}

fun getBulkConnection(connection: PartnerConnection): BulkConnection {
    val config = ConnectorConfig()
    config.sessionId = connection.config.sessionId
    config.restEndpoint = connection.config.serviceEndpoint.replaceAfter("/services", "/async/$API_VERSION")
    return BulkConnection(config)
}

internal fun createConnectorConfig(response: TokenResponse): ConnectorConfig {
    val config = ConnectorConfig()
    config.sessionId = response.accessToken
    config.authEndpoint = "${response.instanceUrl}/services/Soap/u/$API_VERSION"
    config.serviceEndpoint = "${response.instanceUrl}/services/Soap/u/$API_VERSION/${response.id}"
    return config
}

internal fun getTokenResponse(username: String, clientId: String, privateKeyPem: String, baseUrl: String): TokenResponse {
    val jwt = createJWT(username, clientId, privateKeyPem, baseUrl)
    val response = Curl.post("$baseUrl/services/oauth2/token")
            .param("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
            .param("assertion", jwt)
            .execute()
    return parseTokenResponse(response.contentAsStream)
}

internal fun getTokenResponseByPassword(username: String, password: String, securityToken: String, clientId: String, clientSecret: String, baseUrl: String): TokenResponse {
    val response = Curl.post("$baseUrl/services/oauth2/token")
            .param("grant_type", "password")
            .param("username", username)
            .param("password", password + securityToken)
            .param("client_id", clientId)
            .param("client_secret", clientSecret)
            .execute()
    return parseTokenResponse(response.contentAsStream)
}

internal fun getPrivateKey(privateKeyPem: String): RSAPrivateKey {
    val keySpec = PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyPem))
    return KeyFactory.getInstance("RSA").generatePrivate(keySpec) as RSAPrivateKey
}

internal fun createJWT(username: String, clientId: String, privateKeyPem: String, baseUrl: String): String {
    val expire = (System.currentTimeMillis() / 1000) + 300
    val header = """{"alg":"RS256"}"""
    val payload = """{"iss": "$clientId", "sub": "$username", "aud": "$baseUrl", "exp": "$expire"}"""

    val token = StringBuilder()
    token.append(Base64.encodeBase64URLSafeString(header.toByteArray(Charsets.UTF_8)))
    token.append(".")
    token.append(Base64.encodeBase64URLSafeString(payload.toByteArray(Charsets.UTF_8)))

    val privateKey = getPrivateKey(privateKeyPem)
    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(token.toString().toByteArray(Charsets.UTF_8))
    val signedPayload = Base64.encodeBase64URLSafeString(signature.sign())

    token.append(".")
    token.append(signedPayload)

    return token.toString()
}

internal fun parseTokenResponse(content: InputStream): TokenResponse {
    val response: TokenResponse = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(content)
    if (response.accessToken == null) throw SalesforceDataStoreException("${response.error}: ${response.errorDescription}")
    return response
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
internal data class TokenResponse(
        val accessToken: String?,
        val instanceUrl: String?,
        val id: String?,
        val tokenType: String?,
        val error: String?,
        val errorDescription: String?
)