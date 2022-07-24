package com.celebmash.config;

public class RedditProps {
    private String baseURL;
    private String source;
    private String sourceNsfw;
    private String refreshToken;
    private String authHeader;
    private String refreshTokenUrl;

    public String getBaseUrl() {
        return this.baseURL;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseURL = baseUrl;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceNsfw() {
        return this.sourceNsfw;
    }

    public void setSourceNsfw(String sourceNsfw) {
        this.sourceNsfw = sourceNsfw;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAuthHeader() {
        return this.authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public String getRefreshTokenUrl() {
        return this.refreshTokenUrl;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }

}
