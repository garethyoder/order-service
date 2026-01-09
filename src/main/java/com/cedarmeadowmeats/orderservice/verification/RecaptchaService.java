package com.cedarmeadowmeats.orderservice.verification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class RecaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    RestClient restClient = RestClient.create();

    @Value("${recaptcha.secretKey}")
    private String recaptchaSecretKey;

    public RecaptchaResponse verify(SiteVerificationRequest request) throws RestClientException {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", recaptchaSecretKey);
            params.add("response", request.getToken());
            Optional.ofNullable(request.getRemoteIp()).ifPresent(remoteIp -> params.add("response", remoteIp));

            return restClient.post()
                    .uri("https://www.google.com/recaptcha/api/siteverify")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(params)
                    .retrieve()
                    .body(RecaptchaResponse.class);
        } catch (RestClientException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }

}
