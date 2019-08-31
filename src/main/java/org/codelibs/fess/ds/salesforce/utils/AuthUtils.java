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
package org.codelibs.fess.ds.salesforce.utils;

import static org.codelibs.fess.ds.salesforce.SalesforceDataStore.API_VERSION;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.codelibs.curl.Curl;
import org.codelibs.curl.CurlException;
import org.codelibs.curl.CurlResponse;
import org.codelibs.fess.ds.salesforce.SalesforceDataStoreException;
import org.codelibs.fess.ds.salesforce.api.TokenResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public class AuthUtils {
    private static final Logger logger = Logger.getLogger(AuthUtils.class);

    public static PartnerConnection getConnection(final String username, final String clientId,
                                                  final String privateKeyPem, final String baseUrl, final long refreshInterval) throws ConnectionException {
        final TokenResponse response = getTokenResponse(username, clientId, privateKeyPem, baseUrl, refreshInterval);
        if (response.getAccessToken() == null) {
            throw new SalesforceDataStoreException(response.getError() + " : " + response.getErrorDescription());
        }
        final ConnectorConfig config = createConnectorConfig(response);
        return Connector.newConnection(config);
    }

    public static PartnerConnection getConnectionByPassword(final String username, final String password,
                                                            final String securityToken, final String clientId,
                                                            final String clientSecret, final String baseUrl) throws ConnectionException {
        final TokenResponse response = getTokenResponseByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
        final ConnectorConfig config = createConnectorConfig(response);
        return Connector.newConnection(config);
    }

    public static ConnectorConfig createConnectorConfig(final TokenResponse response) {
        final ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(response.getAccessToken());
        config.setAuthEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION);
        config.setServiceEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION + "/" + response.getId());
        return config;
    }

    protected static TokenResponse getTokenResponse(final String username, final String clientId,
                                                        final String privateKeyPem, final String baseUrl, final long refreshInterval) {
        try {
            final String jwt = createJWT(username, clientId, privateKeyPem, baseUrl, refreshInterval);
            final CurlResponse response = Curl.post(baseUrl + "/services/oauth2/token")
                    .param("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                    .param("assertion", jwt)
                    .execute();
            return parseTokenResponse(response.getContentAsStream());
        } catch (final Exception e) {
            throw new SalesforceDataStoreException("Failed to get token response.", e);
        }
    }

    public static TokenResponse refreshToken(final String clientSecret, final String currentToken,
                                                final String baseUrl) {
        try {
            final CurlResponse response = Curl.post(baseUrl + "/services/oauth2/token")
                    .param("grant_type", "refresh_token")
                    .param("client_secret", clientSecret)
                    .param("refresh_token", currentToken)
                    .execute();
            return parseTokenResponse(response.getContentAsStream());
        } catch (final Exception e) {
            throw new SalesforceDataStoreException("Failed to refresh the token.", e);
        }
    }

    public static TokenResponse getTokenResponseByPassword(final String username, final String password, final String securityToken,
                                                       final String clientId, final String clientSecret, final String baseUrl) {
        try {
            final CurlResponse response = Curl.post(baseUrl + "/services/oauth2/token")
                    .param("grant_type", "password")
                    .param("username", username)
                    .param("password", password + securityToken)
                    .param("client_id", clientId)
                    .param("client_secret", clientSecret)
                    .execute();
            return parseTokenResponse(response.getContentAsStream());
        }catch (final CurlException | IOException e) {
            throw new SalesforceDataStoreException("Failed to get response.", e);
        }
    }

    protected static PrivateKey getPrivateKey(final String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String key = privateKeyPem.replaceAll("\\\\n|-----[A-Z ]+-----", "");
        final KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key));
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    protected static String createJWT(final String username, final String clientId, final String privateKeyPem,
                                      final String baseUrl, final long refreshInterval)
            throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException {
        final long expire = (System.currentTimeMillis() / 1000) + refreshInterval;
        final String header = "{\"alg\":\"RS256\"}";
        final String payload = "{\"iss\": \"" + clientId + "\", \"sub\": \"" + username + "\", \"aud\": \"" + baseUrl + "\", \"exp\": \"" + expire + "\"}";

        final StringBuilder token = new StringBuilder();
        token.append(Base64.encodeBase64URLSafeString(header.getBytes(StandardCharsets.UTF_8)));
        token.append(".");
        token.append(Base64.encodeBase64URLSafeString(payload.getBytes(StandardCharsets.UTF_8)));

        final PrivateKey rsaPrivateKey = getPrivateKey(privateKeyPem);
        final Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(rsaPrivateKey);
        signature.update(token.toString().getBytes(StandardCharsets.UTF_8));
        final String signedPayload = Base64.encodeBase64URLSafeString(signature.sign());
        token.append(".");
        token.append(signedPayload);

        return token.toString();
    }

    public static TokenResponse parseTokenResponse(final InputStream content) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(content, TokenResponse.class);
        } catch (final Exception e) {
            throw new SalesforceDataStoreException("Failed to parse token response.", e);
        }
    }

}