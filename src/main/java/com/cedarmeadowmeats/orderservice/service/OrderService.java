package com.cedarmeadowmeats.orderservice.service;

import com.cedarmeadowmeats.orderservice.model.Submission;
import com.cedarmeadowmeats.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveSubmission(final Submission submission) throws NoSuchAlgorithmException {

        if (isHashValidated(submission)) {
            orderRepository.saveSubmission(submission);
        } else {
            try {
                LOGGER.warn("Failed Hash Validation {}", objectMapper.writeValueAsString(submission));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static String MD5_HASH(@NotNull String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter
                .printHexBinary(digest).toUpperCase();
    }

    private boolean isHashValidated(Submission submission) throws NoSuchAlgorithmException {
        return MD5_HASH(submission.getEmail()).equals(submission.getHashKey().toUpperCase());
    }
}
