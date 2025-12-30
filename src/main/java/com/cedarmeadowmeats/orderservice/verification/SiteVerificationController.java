package com.cedarmeadowmeats.orderservice.verification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/verification")
public class SiteVerificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ObjectMapper objectMapper;
    private final RecaptchaService recaptchaService;

    public SiteVerificationController(ObjectMapper objectMapper, RecaptchaService recaptchaService) {
        this.objectMapper = objectMapper;
        this.recaptchaService = recaptchaService;
    }

    @PostMapping("/verify")
    public ResponseEntity<RecaptchaResponse> verify(@RequestBody SiteVerificationRequest request) throws IOException {

        RecaptchaResponse response = recaptchaService.verify(request);

        LOGGER.info("response={}.", objectMapper.writeValueAsString(response));
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            LOGGER.error("Error occurred while sending verification request. {}",
                    String.join(" : ", response.getErrorCodes()) );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
