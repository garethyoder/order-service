package com.cedarmeadowmeats.orderservice;

import com.cedarmeadowmeats.orderservice.model.OrderFormSelectionEnum;
import com.cedarmeadowmeats.orderservice.model.OrderFormSubmissionRequest;
import com.cedarmeadowmeats.orderservice.model.Submission;
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
@ActiveProfiles("test")
public class OrderSubmissionFormTest {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void submitOrder() throws Exception {
        OrderFormSubmissionRequest orderFormSubmissionRequest = new OrderFormSubmissionRequest("test", "testEmail@test.com", "717-236-3456", "test comments", "form-name", "test-org-id", OrderFormSelectionEnum.HALF_STEER);

        this.mockMvc.perform(post("/order-form/submit").accept(MediaType.valueOf("application/json")).contentType(MediaType.APPLICATION_JSON).content(MAPPER.writeValueAsString(orderFormSubmissionRequest))).andExpect(status().isOk());

        // verify the data is created in the DB
        List<Submission> result = orderRepository.getSubmissionByEmail("testEmail@test.com");
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
}
