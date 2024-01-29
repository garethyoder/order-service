package com.cedarmeadowmeats.orderservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "order-service.email")
public record EmailTemplateLocationConfig(String orderFormAlertEmail,
                                          String confirmationToClientEmail,
                                          String defaultAlertEmail) {
}
