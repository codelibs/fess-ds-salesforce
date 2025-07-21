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
package org.codelibs.fess.ds.salesforce.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Represents the response from a Salesforce token request.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenResponse {
    /** The access token. */
    protected String accessToken;
    /** The instance URL. */
    protected String instanceUrl;
    /** The ID. */
    protected String id;
    /** The token type. */
    protected String tokenType;
    /** The error. */
    protected String error;
    /** The error description. */
    protected String errorDescription;

    /**
     * Default constructor.
     */
    public TokenResponse() {
        // Do nothing
    }

    /**
     * Returns the access token.
     *
     * @return The access token.
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Returns the instance URL.
     *
     * @return The instance URL.
     */
    public String getInstanceUrl() {
        return instanceUrl;
    }

    /**
     * Returns the ID.
     *
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the token type.
     *
     * @return The token type.
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Returns the error.
     *
     * @return The error.
     */
    public String getError() {
        return error;
    }

    /**
     * Returns the error description.
     *
     * @return The error description.
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public String toString() {
        return "[accessToken=" + accessToken + ", instanceUrl=" + instanceUrl + ", id=" + id + ", " + "tokenType=" + tokenType + ", error="
                + error + ", errorDescription=" + errorDescription + "]";
    }
}