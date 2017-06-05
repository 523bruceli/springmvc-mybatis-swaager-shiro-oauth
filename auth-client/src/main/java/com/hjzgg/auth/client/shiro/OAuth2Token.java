package com.hjzgg.auth.client.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class OAuth2Token implements AuthenticationToken {
    private String authCode;  
    private String principal;  
    public OAuth2Token(String authCode) {  
        this.authCode = authCode;  
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public String getCredentials() {
        return authCode ;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
}