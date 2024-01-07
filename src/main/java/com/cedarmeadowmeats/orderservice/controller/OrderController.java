package com.cedarmeadowmeats.orderservice.controller;

import com.cedarmeadowmeats.orderservice.model.OrderFormSubmissionRequest;
import com.cedarmeadowmeats.orderservice.model.Submission;
import com.cedarmeadowmeats.orderservice.model.SubmissionRequest;
import com.cedarmeadowmeats.orderservice.service.OrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    public void submit(final @Validated @RequestBody SubmissionRequest submissionRequest) {
        orderService.saveSubmission(new Submission(submissionRequest));
    }

    @PostMapping("/order-form/submit")
    public void submit(final @Validated @RequestBody OrderFormSubmissionRequest orderFormSubmissionRequest) {
        orderService.saveSubmission(new Submission(orderFormSubmissionRequest));
    }
}
