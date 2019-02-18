package org.codelibs.fess.ds.salesforce

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.api.client.util.Base64
import com.google.api.client.util.SecurityUtils
import com.sforce.async.BulkConnection
import com.sforce.ws.ConnectorConfig
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import org.codelibs.curl.Curl
import java.security.Signature
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

object ForceHelper {

    fun getTokenResponse(username: String, clientId: String, privateKeyPem: String,
                         host: String = "https://login.salesforce.com"): TokenResponse {
        val expire = (System.currentTimeMillis() / 1000) + 280
        val header = """{"alg":"RS256"}"""
        val payload = """{"iss": "$clientId", "sub": "$username", "aud": "$host", "exp": "$expire"}"""

        val token = StringBuilder()
        token.append(Base64.encodeBase64URLSafeString(header.toByteArray(Charsets.UTF_8)))
        token.append(".")
        token.append(Base64.encodeBase64URLSafeString(payload.toByteArray(Charsets.UTF_8)))

        val keySpec = PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyPem))
        val keyFactory = SecurityUtils.getRsaKeyFactory()
        val privateKey = keyFactory.generatePrivate(keySpec) as RSAPrivateKey
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKey)
        signature.update(token.toString().toByteArray(Charsets.UTF_8))
        val signedPayload = Base64.encodeBase64URLSafeString(signature.sign())

        token.append(".")
        token.append(signedPayload)

        val response = Curl.post("$host/services/oauth2/token")
                .param("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                .param("assertion", token.toString())
                .execute()
        val tokenResponse = jacksonObjectMapper().readValue(response.contentAsStream, TokenResponse::class.java)
        if (tokenResponse.accessToken == null) throw SalesforceDataStoreException("${tokenResponse.error}: ${tokenResponse.errorDescription}")
        return tokenResponse
    }

    fun getTokenResponseByPassword(username: String, password: String, securityToken: String, clientId: String, clientSecret: String,
                                   host: String = "https://login.salesforce.com"): TokenResponse {
        val response = Curl.post("$host/services/oauth2/token")
                .param("grant_type", "password")
                .param("username", username)
                .param("password", password + securityToken)
                .param("client_id", clientId)
                .param("client_secret", clientSecret)
                .execute()
        val tokenResponse: TokenResponse = jacksonObjectMapper().readValue(response.contentAsStream)
        if (tokenResponse.accessToken == null) throw SalesforceDataStoreException("${tokenResponse.error}: ${tokenResponse.errorDescription}")
        return tokenResponse
    }

    fun getBulkConnection(username: String, clientId: String, privateKeyPem: String,
                          host: String = "https://login.salesforce.com"): BulkConnection {
        val response = getTokenResponse(username, clientId, privateKeyPem, host)
        val config = ConnectorConfig()
        config.sessionId = response.accessToken
        config.restEndpoint = "${response.instanceUrl}/async/45.0"
        return BulkConnection(config)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
    data class TokenResponse(
            val accessToken: String?,
            val instanceUrl: String?,
            val id: String?,
            val tokenType: String?,
            val scope: String?,
            val signature: String?,
            val issuedAt: String?,
            val error: String = "",
            val errorDescription: String = ""
    )
}