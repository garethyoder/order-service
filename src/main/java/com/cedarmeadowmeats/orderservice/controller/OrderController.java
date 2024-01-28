package com.cedarmeadowmeats.orderservice.controller;

import com.cedarmeadowmeats.orderservice.model.OrderFormSubmissionRequest;
import com.cedarmeadowmeats.orderservice.model.Submission;
import com.cedarmeadowmeats.orderservice.model.SubmissionRequest;
import com.cedarmeadowmeats.orderservice.service.OrderService;
import com.cedarmeadowmeats.orderservice.service.SquareService;
import com.squareup.square.models.Customer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RestController
@EnableWebMvc
public class OrderController {

    private final OrderService orderService;
    private final SquareService squareService;

    public OrderController(OrderService orderService, SquareService squareService) {
        this.orderService = orderService;
        this.squareService = squareService;
    }

    @PostMapping("/submit")
    public void submit(final @Validated @RequestBody SubmissionRequest submissionRequest) {
        orderService.saveSubmission(new Submission(submissionRequest));
    }

    @PostMapping("/order-form/submit")
    public void submit(final @Validated @RequestBody OrderFormSubmissionRequest orderFormSubmissionRequest) {
        orderService.saveSubmission(new Submission(orderFormSubmissionRequest));
    }

    @GetMapping("/ping")
    public String ping() {
        return "ok!";
    }

    @GetMapping("/ping2")
    public String ping2() {
        return "ok!";
    }

    @GetMapping("/customers")
    public List<Customer> customers() {
        return squareService.getCustomersByEmail("garethyoder@yahoo.com");
    }
}
