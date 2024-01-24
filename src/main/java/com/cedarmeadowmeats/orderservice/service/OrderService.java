package com.cedarmeadowmeats.orderservice.service;

import com.cedarmeadowmeats.orderservice.model.Submission;
import com.cedarmeadowmeats.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse;

import java.lang.invoke.MethodHandles;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository,
                        EmailService emailService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    public void saveSubmission(final Submission submission) {
        orderRepository.saveSubmission(submission);

        SendEmailResponse alertEmailResponse = emailService.sendSubmissionAlertEmail(submission);

        SendEmailResponse confirmationEmailResponse = emailService.sendEmailConfirmationToClient(submission);
    }
}
