package com.cedarmeadowmeats.orderservice.verification;

public class SiteVerificationRequest {

    public String token;
    public String remoteIp;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }
}
