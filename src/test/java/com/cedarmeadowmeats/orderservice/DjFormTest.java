package com.cedarmeadowmeats.orderservice;

import com.cedarmeadowmeats.orderservice.model.DJContactFormSubmissionRequest;
import com.cedarmeadowmeats.orderservice.model.FormEnum;
import com.cedarmeadowmeats.orderservice.model.OrganizationIdEnum;
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
@ActiveProfiles("tc")
public class DjFormTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void submitDJForm() throws Exception {
        DJContactFormSubmissionRequest djContactFormSubmissionRequest = new DJContactFormSubmissionRequest("Joe Doe", "joedoe@test.com", "123-456-7890", "test comments", FormEnum.DJ_FORM, OrganizationIdEnum.G_YODER_AUDIO_EXPRESSIONS, "05-17-2024", "My house");

        this.mockMvc.perform(post("/dj-form/submit").accept(MediaType.valueOf("application/json")).contentType(MediaType.APPLICATION_JSON).content(MAPPER.writeValueAsString(djContactFormSubmissionRequest))).andExpect(status().isOk());

        // verify the data is created in the DB
        List<Submission> result = orderRepository.getSubmissionByEmail("joedoe@test.com");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(djContactFormSubmissionRequest.getName(), result.getFirst().getName());
        Assertions.assertEquals(djContactFormSubmissionRequest.getEmail(), result.getFirst().getEmail());
        Assertions.assertEquals(djContactFormSubmissionRequest.getPhone(), result.getFirst().getPhone());
        Assertions.assertEquals(djContactFormSubmissionRequest.getForm(), result.getFirst().getForm());
        Assertions.assertEquals(djContactFormSubmissionRequest.getEventDate(), result.getFirst().getEventDate());
        Assertions.assertEquals(djContactFormSubmissionRequest.getOrganizationId(), result.getFirst().getOrganizationId());
        Assertions.assertEquals(djContactFormSubmissionRequest.getVenue(), result.getFirst().getVenue());
        Assertions.assertNotNull(result.getFirst().getCreatedDate());
        Assertions.assertNotNull(result.getFirst().getLastUpdatedDate());
        Assertions.assertNotNull(result.getFirst().getVersion());
    }
}
