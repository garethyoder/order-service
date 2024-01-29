package com.cedarmeadowmeats.orderservice.service;

import com.cedarmeadowmeats.orderservice.config.EmailTemplateLocationConfig;
import com.cedarmeadowmeats.orderservice.model.EmailTemplate;
import com.cedarmeadowmeats.orderservice.model.FormEnum;
import com.cedarmeadowmeats.orderservice.model.OrganizationIdEnum;
import com.cedarmeadowmeats.orderservice.model.Submission;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SesV2Client sesV2Client;

    private final EmailTemplateLocationConfig emailTemplateLocationConfig;

    @Value("${order-service.email.sender:}")
    String sender;

    @Value("${order-service.email.no-reply-sender:}")
    String noReplySender;

    @Value("${order-service.email.admin-emails:}")
    List<String> adminEmails;

    public EmailService(SesV2Client sesV2Client, EmailTemplateLocationConfig emailTemplateLocationConfig) {
        this.sesV2Client = sesV2Client;
        this.emailTemplateLocationConfig = emailTemplateLocationConfig;
    }

    public SendEmailResponse sendSubmissionAlertEmail(final Submission submission) throws SesV2Exception {

        EmailTemplate template = null;
        try {
            template = getAlertEmailBody(submission);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SendEmailResponse sendEmailResponse = send(sesV2Client, noReplySender, adminEmails, submission.getEmail(), template.subject(), template.body());
        LOGGER.info("Email sent successfully");
        return sendEmailResponse;
    }

    public SendEmailResponse sendEmailConfirmationToClient(final Submission submission) throws SesV2Exception {

        EmailTemplate template = null;
        try {
            template = getCustomerConfirmationEmailTemplate(submission);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SendEmailResponse sendEmailResponse = send(sesV2Client, sender, Collections.singletonList(submission.getEmail()), sender, template.subject(), template.body());
        LOGGER.info("Email sent successfully");
        return sendEmailResponse;
    }

    public static SendEmailResponse send(SesV2Client client,
                            String sender,
                            List<String> recipients,
                            String replyToAddresses,
                            String subject,
                            String bodyHTML) throws SesV2Exception {

        Destination destination = Destination.builder()
                .toAddresses(recipients)
                .build();

        Content content = Content.builder()
                .data(bodyHTML)
                .build();

        Content sub = Content.builder()
                .data(subject)
                .build();

        Body body = Body.builder()
                .html(content)
                .build();

        Message msg = Message.builder()
                .subject(sub)
                .body(body)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .content(EmailContent.builder().simple(msg).build())
                .fromEmailAddress(sender)
                .replyToAddresses(replyToAddresses)
                .build();

        try {
            LOGGER.info("Attempting to send an email through Amazon SES " + "using the AWS SDK for Java...");
            return client.sendEmail(emailRequest);

        } catch (SesV2Exception e) {
            if (e.getMessage().contains("API for service 'sesv2' not yet implemented or pro feature")) {
                LOGGER.info(e.getMessage());
                return null;
            } else {
                LOGGER.error(e.awsErrorDetails().errorMessage());
                throw e;
            }
        }
    }

    public EmailTemplate getAlertEmailBody(@NotNull final Submission submission) throws IOException {
        String subject = OrganizationIdEnum.getCompanyName(submission.getOrganizationId()) + " " +
                FormEnum.getFormName(submission.getForm()) + ": " +
                submission.getName();

        String body;
        if (FormEnum.ORDER_FORM.equals(submission.getForm())) {
            body = Files.readString(Path.of(emailTemplateLocationConfig.orderFormAlertEmail()))
                    .replace("${companyName}", OrganizationIdEnum.getCompanyName(submission.getOrganizationId()))
                    .replace("${name}", submission.getName())
                    .replace("${phone}", submission.getPhone())
                    .replace("${email}", submission.getEmail())
                    .replace("${selection}", submission.getOrderFormSelectionEnum().getValue())
                    .replace("${comments}", submission.getComments());
        } else {
            body = Files.readString(Path.of(emailTemplateLocationConfig.defaultAlertEmail()))
                    .replace("${companyName}", OrganizationIdEnum.getCompanyName(submission.getOrganizationId()))
                    .replace("${name}", submission.getName())
                    .replace("${phone}", submission.getPhone())
                    .replace("${email}", submission.getEmail())
                    .replace("${comments}", submission.getComments());
        }



        return new EmailTemplate(subject, body);
    }

    public EmailTemplate getCustomerConfirmationEmailTemplate(@NotNull final Submission submission) throws IOException {
        String subject = "Thank you for contacting " + OrganizationIdEnum.getCompanyName(submission.getOrganizationId());

        String body = Files.readString(Path.of(emailTemplateLocationConfig.confirmationToClientEmail()))
                .replace("${companyName}", OrganizationIdEnum.getCompanyName(submission.getOrganizationId()))
                .replace("${name}", submission.getName())
                .replace("${phone}", submission.getPhone())
                .replace("${email}", submission.getEmail())
                .replace("${comments}", submission.getComments());

        return new EmailTemplate(subject, body);
    }

}
