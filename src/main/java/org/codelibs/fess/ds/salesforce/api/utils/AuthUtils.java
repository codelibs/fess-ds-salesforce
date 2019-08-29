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
package org.codelibs.fess.ds.salesforce.api.utils;

import com.sforce.async.AsyncApiException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.async.BulkConnection;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
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
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public class AuthUtils {
    private static final Logger logger = Logger.getLogger(AuthUtils.class);

    protected static String BASE_URL = "https://login.salesforce.com";
    protected static String API_VERSION = "45.0";

    public PartnerConnection getConnection(String username, String clientId, String privateKeyPem) {
        return getConnection(username, clientId, privateKeyPem, BASE_URL);
    }

    public static PartnerConnection getConnection(String username, String clientId,
                                           String privateKeyPem, String baseUrl) {
        TokenResponse response = getTokenResponse(username, clientId, privateKeyPem, baseUrl);
        ConnectorConfig config = createConnectorConfig(response);
        try {
            return Connector.newConnection(config);
       } catch (Exception e) {
            // TODO
            throw new SalesforceDataStoreException(e);
        }
    }

    public static PartnerConnection getConnectionByPassword(String username, String password,
                                                     String securityToken, String clientId,
                                                     String clientSecret) {
        return getConnectionByPassword(username, password, securityToken, clientId, clientSecret, BASE_URL);
    }

    public static PartnerConnection getConnectionByPassword(String username, String password,
                                                     String securityToken, String clientId,
                                                     String clientSecret, String baseUrl) {
        TokenResponse response = getTokenResponseByPassword(username, password, securityToken, clientId, clientSecret, baseUrl);
        ConnectorConfig config = createConnectorConfig(response);
        try {
            return Connector.newConnection(config);
        } catch (Exception e) {
            // TODO
            throw new SalesforceDataStoreException(e);
        }
    }

    public static BulkConnection getBulkConnection(PartnerConnection connection) throws AsyncApiException {
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(connection.getConfig().getSessionId());
        config.setRestEndpoint(connection.getConfig().getServiceEndpoint()
                .replaceFirst("/services.*", "/services/async/" + API_VERSION));
        return new BulkConnection(config);
    }

    protected static ConnectorConfig createConnectorConfig(final TokenResponse response) {
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(response.getAccessToken());
        config.setAuthEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION);
        config.setServiceEndpoint(response.getInstanceUrl() + "/services/Soap/u/" + API_VERSION + "/" + response.getId());
        return config;
    }

    public static TokenResponse getTokenResponse(final String username, final String clientId,
                                             final String privateKeyPem, final String baseUrl) {
        try {
            String jwt = createJWT(username, clientId, privateKeyPem, baseUrl);
            CurlResponse response = Curl.post(baseUrl + "/services/oauth2/token")
                    .param("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                    .param("assertion", jwt)
                    .execute();
            try {
                return parseTokenResponse(response.getContentAsStream());
            } catch(IOException e) {
                throw new SalesforceDataStoreException("Failed to parse a token response.", e);
            }
        } catch (CurlException e) {
            throw new SalesforceDataStoreException(e);
        }
    }

    public static TokenResponse getTokenResponseByPassword(final String username, final String password, final String securityToken,
                                                       final String clientId, final String clientSecret, final String baseUrl) {
        try {
            CurlResponse response = Curl.post(baseUrl + "/services/oauth2/token")
                    .param("grant_type", "password")
                    .param("username", username)
                    .param("password", password + securityToken)
                    .param("client_id", clientId)
                    .param("client_secret", clientSecret)
                    .execute();
            try {
                return parseTokenResponse(response.getContentAsStream());
            } catch(IOException e) {
                throw new SalesforceDataStoreException("Failed to parse a token response.", e);
            }
        }catch (CurlException e) {
            throw new SalesforceDataStoreException("Failed to get response.", e);
        }
    }

    public static PrivateKey getPrivateKey(final String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String key = privateKeyPem.replaceAll("\\\\n|-----[A-Z ]+-----", "");
        KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key));
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    public static String createJWT(final String username, final String clientId, final String privateKeyPem, final String baseUrl) {
        long expire = (System.currentTimeMillis() / 1000) + 300;
        String header = "{\"alg\":\"RS256\"}";
        String payload = "{\"iss\": \"" + clientId + "\", \"sub\": \"" + username + "\", \"aud\": \"" + baseUrl + "\", \"exp\": \"" + expire + "\"}";

        StringBuilder token = new StringBuilder();
        token.append(Base64.encodeBase64URLSafeString(header.getBytes(StandardCharsets.UTF_8)));
        token.append(".");
        token.append(Base64.encodeBase64URLSafeString(payload.getBytes(StandardCharsets.UTF_8)));

        try {
            PrivateKey rsaPrivateKey = getPrivateKey(privateKeyPem);
            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initSign(rsaPrivateKey);
            signature.update(token.toString().getBytes(StandardCharsets.UTF_8));
            String signedPayload = Base64.encodeBase64URLSafeString(signature.sign());
            token.append(".");
            token.append(signedPayload);
            return token.toString();
        } catch (Exception e) {
            throw new SalesforceDataStoreException("Failed to create the JSON Web Token.", e);
        }
    }


    public static TokenResponse parseTokenResponse(final InputStream content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TokenResponse response = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(content, TokenResponse.class);
            if (response.getAccessToken() == null) {
                throw new SalesforceDataStoreException(response.getError() + " : " + response.getErrorDescription());
            }
            return response;
        }catch (Exception e) {
            throw new SalesforceDataStoreException("Failed to parse the content.", e);
        }
    }

}