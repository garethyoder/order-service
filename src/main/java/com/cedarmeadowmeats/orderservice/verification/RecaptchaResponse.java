package com.cedarmeadowmeats.orderservice.verification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecaptchaResponse {

    public boolean success;
    @JsonProperty("challenge_ts")
    public String challengeTs;
    public String hostname;
    @JsonProperty("error-codes")
    public List<String> errorCodes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public void setChallengeTs(String challengeTs) {
        this.challengeTs = challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
