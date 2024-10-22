package com.cedarmeadowmeats.orderservice;

import com.cedarmeadowmeats.orderservice.model.*;
import com.cedarmeadowmeats.orderservice.repository.OrderRepository;
import com.cedarmeadowmeats.orderservice.testcontainers.TestContainersConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import({TestContainersConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("tc")
public class OrderSubmissionFormTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void submitOrder() throws Exception {
        OrderFormSubmissionRequest orderFormSubmissionRequest = new OrderFormSubmissionRequest("John Smith", "johnsmith@test.com", "123-456-7890", "test comments", FormEnum.ORDER_FORM, OrganizationIdEnum.CEDAR_MEADOW_MEATS, OrderFormSelectionEnum.HALF_STEER, null);

        this.mockMvc.perform(post("/order-form/submit").accept(MediaType.valueOf("application/json")).contentType(MediaType.APPLICATION_JSON).content(MAPPER.writeValueAsString(orderFormSubmissionRequest))).andExpect(status().isOk());

        // verify the data is created in the DB
        List<Submission> result = orderRepository.getSubmissionByEmail("johnsmith@test.com");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(orderFormSubmissionRequest.getName(), result.getFirst().getName());
        Assertions.assertEquals(orderFormSubmissionRequest.getEmail(), result.getFirst().getEmail());
        Assertions.assertEquals(orderFormSubmissionRequest.getPhone(), result.getFirst().getPhone());
        Assertions.assertEquals(orderFormSubmissionRequest.getForm(), result.getFirst().getForm());
        Assertions.assertEquals(orderFormSubmissionRequest.getOrganizationId(), result.getFirst().getOrganizationId());
        Assertions.assertEquals(orderFormSubmissionRequest.getOrderFormSelectionEnum(), result.getFirst().getOrderFormSelectionEnum());
        Assertions.assertNotNull(result.getFirst().getCreatedDate());
        Assertions.assertNotNull(result.getFirst().getLastUpdatedDate());
        Assertions.assertNotNull(result.getFirst().getVersion());

    }

    @Test
    public void submitOrderWithReferral() throws Exception {
        OrderFormSubmissionRequest orderFormSubmissionRequest = new OrderFormSubmissionRequest("John Doe", "johndoe@test.com", "123-456-7890", "test comments", FormEnum.ORDER_FORM, OrganizationIdEnum.CEDAR_MEADOW_MEATS, OrderFormSelectionEnum.HALF_STEER, "Jane Doe");

        this.mockMvc.perform(post("/order-form/submit").accept(MediaType.valueOf("application/json")).contentType(MediaType.APPLICATION_JSON).content(MAPPER.writeValueAsString(orderFormSubmissionRequest))).andExpect(status().isOk());

        // verify the data is created in the DB
        List<Submission> result = orderRepository.getSubmissionByEmail("johndoe@test.com");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(orderFormSubmissionRequest.getName(), result.getFirst().getName());
        Assertions.assertEquals(orderFormSubmissionRequest.getEmail(), result.getFirst().getEmail());
        Assertions.assertEquals(orderFormSubmissionRequest.getPhone(), result.getFirst().getPhone());
        Assertions.assertEquals(orderFormSubmissionRequest.getForm(), result.getFirst().getForm());
        Assertions.assertEquals(orderFormSubmissionRequest.getReferral(), result.getFirst().getReferral());
        Assertions.assertEquals(orderFormSubmissionRequest.getOrganizationId(), result.getFirst().getOrganizationId());
        Assertions.assertEquals(orderFormSubmissionRequest.getOrderFormSelectionEnum(), result.getFirst().getOrderFormSelectionEnum());
        Assertions.assertNotNull(result.getFirst().getCreatedDate());
        Assertions.assertNotNull(result.getFirst().getLastUpdatedDate());
        Assertions.assertNotNull(result.getFirst().getVersion());
    }

}
