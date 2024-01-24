package com.cedarmeadowmeats.orderservice;

import com.cedarmeadowmeats.orderservice.model.*;
import com.cedarmeadowmeats.orderservice.repository.OrderRepository;
import com.cedarmeadowmeats.orderservice.testcontainers.TestContainersConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;
import software.amazon.awssdk.services.sesv2.model.SendEmailResponse;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import({TestContainersConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("tc")
public class OrderSubmissionFormTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @MockBean
    private SesV2Client sesV2Client;
    @Captor
    private ArgumentCaptor<SendEmailRequest> sendEmailRequestArgumentCaptor;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        SendEmailResponse sendEmailResponse = SendEmailResponse.builder().build();
        doReturn(sendEmailResponse).when(sesV2Client).sendEmail(sendEmailRequestArgumentCaptor.capture());
    }

    @Test
    public void submitOrder() throws Exception {
        OrderFormSubmissionRequest orderFormSubmissionRequest = new OrderFormSubmissionRequest("John Smith", "client@test.com", "123-456-7890", "test comments", FormEnum.ORDER_FORM, OrganizationIdEnum.CEDAR_MEADOW_MEATS, OrderFormSelectionEnum.HALF_STEER);

        this.mockMvc.perform(post("/order-form/submit").accept(MediaType.valueOf("application/json")).contentType(MediaType.APPLICATION_JSON).content(MAPPER.writeValueAsString(orderFormSubmissionRequest))).andExpect(status().isOk());

        // verify the data is created in the DB
        List<Submission> result = orderRepository.getSubmissionByEmail("client@test.com");
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

        verify(sesV2Client, times(2)).sendEmail(any(SendEmailRequest.class));

        SendEmailRequest alertEmail = sendEmailRequestArgumentCaptor.getAllValues().getFirst();
        SendEmailRequest confirmationEmail = sendEmailRequestArgumentCaptor.getAllValues().get(1);

        // Alert Email Assertions
        assertThat(alertEmail.destination().toAddresses(), hasItems("Gareth Yoder <gyoder@cedarmeadowmeats.com>"));
        assertThat(alertEmail.destination().toAddresses(), hasItems("Joy Yoder <jyoder@cedarmeadowmeats.com>"));
        Assertions.assertEquals("noReply <noReply@cedarmeadowmeats.com>", alertEmail.fromEmailAddress(), "Verify the \"noReply\" from sender email.");
        Assertions.assertEquals(orderFormSubmissionRequest.getEmail(), alertEmail.replyToAddresses().getFirst(), "Verify the reply to is the client email.");
        Assertions.assertEquals("Cedar Meadow Meats Order Form: John Smith", alertEmail.content().simple().subject().data(), "Verify the email subject line.");
        assertThat("Alert email must contain client's name.", alertEmail.content().simple().body().toString(), containsString(orderFormSubmissionRequest.getName()));
        assertThat("Alert email must contain client's phone.", alertEmail.content().simple().body().toString(), containsString(orderFormSubmissionRequest.getPhone()));
        assertThat("Alert email must contain client's email.", alertEmail.content().simple().body().toString(), containsString(orderFormSubmissionRequest.getEmail()));
        assertThat("Alert email must contain client selection.", alertEmail.content().simple().body().toString(), containsString(orderFormSubmissionRequest.getOrderFormSelectionEnum().getValue()));
        assertThat("Alert email must contain comments.", alertEmail.content().simple().body().toString(), containsString(orderFormSubmissionRequest.getComments()));

        // Confirmation Email Assertions
        Assertions.assertEquals(orderFormSubmissionRequest.getEmail(), confirmationEmail.destination().toAddresses().getFirst(), "Verify the destination email is the client.");
        Assertions.assertEquals("Gareth Yoder <gyoder@cedarmeadowmeats.com>", confirmationEmail.fromEmailAddress(), "Verify the sender email is the admin.");
        Assertions.assertEquals("Gareth Yoder <gyoder@cedarmeadowmeats.com>", confirmationEmail.replyToAddresses().getFirst(), "Verify the reply to is the admin.");
        Assertions.assertEquals("Thank you for contacting Cedar Meadow Meats", confirmationEmail.content().simple().subject().data(), "Verify the email subject line.");
        assertThat("Confirmation email body.", confirmationEmail.content().simple().body().toString(), containsString("Thank you for reaching out to Cedar Meadow Meats.  We typically respond in 1-2 business days."));
    }
}
