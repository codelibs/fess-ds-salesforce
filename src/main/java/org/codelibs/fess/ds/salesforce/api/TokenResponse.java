package org.codelibs.fess.ds.salesforce.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TokenResponse {
    protected String accessToken;
    protected String instanceUrl;
    protected String id;
    protected String tokenType;
    protected String error;
    protected String errorDescription;

    public String getAccessToken() {
        return accessToken;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public String getId() {
        return id;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public String toString() {
        return "[accessToken=" + accessToken + ", instanceUrl=" + instanceUrl + ", id=" + id + ", " +
                "tokenType=" + tokenType + ", error=" + error + ", errorDescription=" + errorDescription + "]";
    }
}
