/*
 * Copyright 2012-2025 CodeLibs Project and the Others.
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
package org.codelibs.fess.ds.salesforce.util;

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

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * Utility class for Salesforce authentication.
 */
public class AuthUtil {

    private static final Logger logger = Logger.getLogger(AuthUtil.class);

    private AuthUtil() {
        throw new IllegalStateException("Utility class.");
    }

    /**
     * Generates a {@link PrivateKey} object from a PEM format string.
     *
     * @param privateKeyPem The private key in PEM format.
     * @return A {@link PrivateKey} object.
     * @throws NoSuchAlgorithmException If the RSA algorithm is not available.
     * @throws InvalidKeySpecException If the key specification is invalid.
     */
    public static PrivateKey getPrivateKey(final String privateKeyPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String key = privateKeyPem.replaceAll("\\\\n|-----[A-Z ]+-----", "");
        final KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key));
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    /**
     * Creates a JWT (JSON Web Token) for Salesforce authentication.
     *
     * @param username The Salesforce username.
     * @param clientId The connected app's consumer key.
     * @param privateKeyPem The private key in PEM format for signing.
     * @param baseUrl The Salesforce instance URL (e.g., https://login.salesforce.com).
     * @param refreshInterval The token expiration interval in seconds.
     * @return The generated JWT as a String.
     * @throws InvalidKeyException If the key is invalid.
     * @throws InvalidKeySpecException If the key specification is invalid.
     * @throws NoSuchAlgorithmException If the signing algorithm is not available.
     * @throws SignatureException If an error occurs during the signing process.
     */
    public static String createJWT(final String username, final String clientId, final String privateKeyPem, final String baseUrl,
            final long refreshInterval) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException {
        final StringBuilder token = new StringBuilder();

        final String header = "{\"alg\":\"RS256\"}";
        token.append(Base64.encodeBase64URLSafeString(header.getBytes(StandardCharsets.UTF_8)));
        token.append(".");

        final long expire = (System.currentTimeMillis() / 1000) + refreshInterval;
        final String payload = "{\"iss\": \"" + clientId + "\"," + " \"sub\": \"" + username + "\"," + " \"aud\": \"" + baseUrl + "\","
                + " \"exp\": \"" + expire + "\"}";
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

}
