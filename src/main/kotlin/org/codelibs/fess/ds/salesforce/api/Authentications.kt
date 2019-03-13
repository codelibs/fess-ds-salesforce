package org.codelibs.fess.ds.salesforce.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.api.client.util.Base64
import com.google.api.client.util.SecurityUtils
import com.sforce.async.BulkConnection
import com.sforce.soap.partner.Connector
import com.sforce.soap.partner.PartnerConnection
import com.sforce.ws.ConnectorConfig
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import org.codelibs.curl.Curl
import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException
import java.io.InputStream
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

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
    val keyFactory = SecurityUtils.getRsaKeyFactory()
    return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
}

internal fun createJWT(username: String, clientId: String, privateKeyPem: String, baseUrl: String): String {
    val privateKey = getPrivateKey(privateKeyPem)
    val now = System.currentTimeMillis()
    return JWT.create()
            .withIssuer(clientId)
            .withSubject(username)
            .withAudience(baseUrl)
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + 300 * 1000))
            .sign(Algorithm.RSA256(null, privateKey))
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