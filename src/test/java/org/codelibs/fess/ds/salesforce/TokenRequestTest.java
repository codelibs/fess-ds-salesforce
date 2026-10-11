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
package org.codelibs.fess.ds.salesforce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.codelibs.curl.CurlRequest;
import org.codelibs.fess.ds.salesforce.SalesforceClient.ConnectionProvider;
import org.codelibs.fess.ds.salesforce.api.TokenResponse;
import org.codelibs.fess.entity.DataStoreParams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

/**
 * Verifies how the OAuth token request is sent and how its failures are reported, against a local stub server.
 */
public class TokenRequestTest {

    private static final String USERNAME = "sforce@example.com";
    private static final String PASSWORD = "PwValue&1=2 x";
    private static final String SECURITY_TOKEN = "SecTokenValue9";
    private static final String CLIENT_ID = "ClientIdValue3";
    private static final String CLIENT_SECRET = "ClientSecretValue7";

    private static final String[] SECRETS = { PASSWORD, SECURITY_TOKEN, CLIENT_SECRET };

    private HttpServer server;
    private final AtomicReference<String> requestMethod = new AtomicReference<>();
    private final AtomicReference<String> requestQuery = new AtomicReference<>();
    private final AtomicReference<String> requestContentType = new AtomicReference<>();
    private final AtomicReference<String> requestBody = new AtomicReference<>();
    private volatile int responseStatus = 200;
    private volatile String responseContentType = "application/json";
    private volatile String responseBody = "{}";

    private final List<String> curlLogs = new ArrayList<>();
    private Handler curlLogHandler;
    private Logger curlLogger;
    private Level curlLogLevel;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new java.net.InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/services/oauth2/token", exchange -> {
            requestMethod.set(exchange.getRequestMethod());
            requestQuery.set(exchange.getRequestURI().getRawQuery());
            requestContentType.set(exchange.getRequestHeaders().getFirst("Content-Type"));
            requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            final byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", responseContentType);
            exchange.sendResponseHeaders(responseStatus, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        server.start();

        // curl4j logs every request verbatim at FINE through java.util.logging
        curlLogger = Logger.getLogger(CurlRequest.class.getName());
        curlLogLevel = curlLogger.getLevel();
        curlLogHandler = new Handler() {
            @Override
            public void publish(final LogRecord record) {
                synchronized (curlLogs) {
                    curlLogs.add(record.getMessage());
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
        curlLogHandler.setLevel(Level.ALL);
        curlLogger.setLevel(Level.ALL);
        curlLogger.addHandler(curlLogHandler);
    }

    @AfterEach
    void tearDown() {
        curlLogger.removeHandler(curlLogHandler);
        curlLogger.setLevel(curlLogLevel);
        server.stop(0);
    }

    private String baseUrl() {
        return "http://" + server.getAddress().getAddress().getHostAddress() + ":" + server.getAddress().getPort();
    }

    private DataStoreParams passwordParams(final String baseUrl) {
        return params(baseUrl, "oauth_password");
    }

    private DataStoreParams params(final String baseUrl, final String authType) {
        final DataStoreParams params = new DataStoreParams();
        params.put("base_url", baseUrl);
        if (authType != null) {
            params.put("auth_type", authType);
        }
        params.put("username", USERNAME);
        params.put("password", PASSWORD);
        params.put("security_token", SECURITY_TOKEN);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        return params;
    }

    private static String stackTrace(final Throwable t) {
        final StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /** Everything an ERROR log of the thrown exception would print, plus what curl4j logged. */
    private String everythingLogged(final Throwable t) {
        final StringBuilder sb = new StringBuilder(stackTrace(t));
        synchronized (curlLogs) {
            curlLogs.forEach(sb::append);
        }
        return sb.toString();
    }

    private static void assertNoSecrets(final String text) {
        for (final String secret : SECRETS) {
            assertFalse(text.contains(secret), "raw secret leaked: " + secret + " in " + text);
            final String encoded = URLEncoder.encode(secret, StandardCharsets.UTF_8);
            assertFalse(text.contains(encoded), "encoded secret leaked: " + encoded + " in " + text);
        }
    }

    private SalesforceDataStoreException connectionFailure(final String baseUrl) {
        return assertThrows(SalesforceDataStoreException.class, () -> new ConnectionProvider(passwordParams(baseUrl)));
    }

    // ---- credentials are sent in the form body, not in the URL ----

    @Test
    void passwordFlowSendsCredentialsInFormBody() {
        responseBody = "{\"access_token\":\"tok\",\"instance_url\":\"https://x.example.com\",\"id\":\"i\",\"token_type\":\"Bearer\"}";
        final ConnectionProvider provider = new ConnectionProvider(passwordParams(baseUrl())) {
            @Override
            protected com.sforce.soap.partner.PartnerConnection getConnection() {
                return null;
            }

            @Override
            protected com.sforce.async.BulkConnection getBulkConnection(final com.sforce.soap.partner.PartnerConnection connection) {
                return null;
            }
        };

        final TokenResponse response = provider.getTokenResponseByPass();

        assertEquals("tok", response.getAccessToken());
        assertEquals("POST", requestMethod.get());
        assertNull(requestQuery.get(), "no credential may be in the query string");
        assertNotNull(requestContentType.get());
        assertTrue(requestContentType.get().startsWith("application/x-www-form-urlencoded"), requestContentType.get());
        final String body = requestBody.get();
        assertTrue(body.contains("grant_type=password"), body);
        assertTrue(body.contains("username=" + URLEncoder.encode(USERNAME, StandardCharsets.UTF_8)), body);
        assertTrue(body.contains("password=" + URLEncoder.encode(PASSWORD + SECURITY_TOKEN, StandardCharsets.UTF_8)), body);
        assertTrue(body.contains("client_id=" + CLIENT_ID), body);
        assertTrue(body.contains("client_secret=" + CLIENT_SECRET), body);
        assertNoSecrets(String.join("\n", curlLogs));
    }

    // ---- credentials do not reach the exception message or the log ----

    @Test
    void connectionRefusedDoesNotLeakCredentials() throws IOException {
        final int port;
        try (ServerSocket s = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            port = s.getLocalPort();
        }

        final SalesforceDataStoreException e = connectionFailure("http://127.0.0.1:" + port);

        assertNoSecrets(everythingLogged(e));
    }

    @Test
    void connectionDroppedDoesNotLeakCredentials() throws Exception {
        try (ServerSocket s = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
            // HttpURLConnection retries a dropped POST once, so keep accepting until the socket is closed
            final Thread t = new Thread(() -> {
                try {
                    while (true) {
                        try (Socket c = s.accept()) {
                            c.setSoLinger(true, 0);
                            c.getInputStream().read(new byte[16]);
                        }
                    }
                } catch (final IOException ignored) {
                    // the server socket was closed
                }
            });
            t.setDaemon(true);
            t.start();

            final SalesforceDataStoreException e = connectionFailure("http://127.0.0.1:" + s.getLocalPort());

            assertNoSecrets(everythingLogged(e));
        }
    }

    @Test
    void unauthorizedDoesNotLeakCredentials() {
        responseStatus = 401;
        responseBody = "{\"error\":\"invalid_grant\",\"error_description\":\"authentication failure\"}";

        final SalesforceDataStoreException e = connectionFailure(baseUrl());

        assertNoSecrets(everythingLogged(e));
    }
}
