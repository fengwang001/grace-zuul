package com.qiyi.zuul.config;

import java.util.List;

public class SecurityConfig {
    /**
     * 这里匹配 无需进行Token认证
     */
    private List<String> ignoredPaths;

    private String tokenHeader;

    private String loginUrl;

    public List<String> getIgnoredPaths() {
        return ignoredPaths;
    }

    public void setIgnoredPaths(List<String> ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
