package com.cedarmeadowmeats.orderservice;

import com.cedarmeadowmeats.orderservice.model.FormEnum;
import com.cedarmeadowmeats.orderservice.model.OrganizationIdEnum;
import com.cedarmeadowmeats.orderservice.model.Submission;
import com.cedarmeadowmeats.orderservice.model.SubmissionRequest;
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
public class SubmissionFormTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void submitOrder() throws Exception {
        SubmissionRequest submissionRequest = new SubmissionRequest("John Doe", "client@test.com", "717-236-3456", "test comments", FormEnum.CONTACT_FORM, OrganizationIdEnum.CEDAR_MEADOW_NATURALS, "b0b6533b4732fcf8253b9a8f76497a01");

        this.mockMvc.perform(post("/submit").accept(MediaType.valueOf("application/json")).contentType(MediaType.APPLICATION_JSON).content(MAPPER.writeValueAsString(submissionRequest))).andExpect(status().isOk());

        // verify the data is created in the DB
        List<Submission> result = orderRepository.getSubmissionByEmail("client@test.com");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(submissionRequest.getName(), result.getFirst().getName());
        Assertions.assertEquals(submissionRequest.getEmail(), result.getFirst().getEmail());
        Assertions.assertEquals(submissionRequest.getPhone(), result.getFirst().getPhone());
        Assertions.assertEquals(submissionRequest.getForm(), result.getFirst().getForm());
        Assertions.assertEquals(submissionRequest.getOrganizationId(), result.getFirst().getOrganizationId());
        Assertions.assertNotNull(result.getFirst().getCreatedDate());
        Assertions.assertNotNull(result.getFirst().getLastUpdatedDate());
        Assertions.assertNotNull(result.getFirst().getVersion());
        Assertions.assertFalse(result.getFirst().getSpam());
    }
}
