package com.cedarmeadowmeats.orderservice.service;

import com.cedarmeadowmeats.orderservice.model.Submission;
import com.cedarmeadowmeats.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;

import static com.cedarmeadowmeats.orderservice.service.OrderService.MD5_HASH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ObjectMapper objectMapper;

    @Test
    void testSaveSubmissionPassed() throws NoSuchAlgorithmException {
        Submission submission = new Submission();
        submission.setEmail("client@test.com");
        submission.setHashKey("b0b6533b4732fcf8253b9a8f76497a01");
        orderService.saveSubmission(submission);

        verify(orderRepository).saveSubmission(any());
    }

    @Test
    void testSaveSubmissionFailed() throws NoSuchAlgorithmException, JsonProcessingException {

        when(objectMapper.writeValueAsString(any())).thenReturn("Mapper String Here");

        Submission submission = new Submission();
        submission.setEmail("");
        submission.setHashKey("Failed");
        orderService.saveSubmission(submission);

        verify(orderRepository, never()).saveSubmission(any());
    }

    @Test
    public void testMD5_HASH() throws NoSuchAlgorithmException {
        Assertions.assertEquals("5f4dcc3b5aa765d61d8327deb882cf99".toUpperCase(), MD5_HASH("password"));
        Assertions.assertEquals("02b3b173461c7dbfce5743d7b0023d75".toUpperCase(), MD5_HASH("johndoe@test.com"));
        Assertions.assertEquals("7215ee9c7d9dc229d2921a40e899ec5f".toUpperCase(), MD5_HASH(" "));
    }
}