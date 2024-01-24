package com.cedarmeadowmeats.orderservice.repository;

import com.cedarmeadowmeats.orderservice.model.FormEnum;
import com.cedarmeadowmeats.orderservice.model.OrderFormSelectionEnum;
import com.cedarmeadowmeats.orderservice.model.OrganizationIdEnum;
import com.cedarmeadowmeats.orderservice.model.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.lang.invoke.MethodHandles;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo;

@Repository
public class OrderRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${dynamodb.tableName}")
    private String tableName;

    private final DynamoDbClient dynamoDbClient;

    public OrderRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void saveSubmission(final Submission submission) {
        LOGGER.info(submission.toString());
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
        PutItemEnhancedRequest<Submission> putItemEnhancedRequest = PutItemEnhancedRequest.builder(Submission.class).item(submission).build();
        WriteBatch writeBatch = WriteBatch.builder(Submission.class)
                .mappedTableResource(enhancedClient.table(tableName, SUBMISSION_STATIC_TABLE_SCHEMA))
                .addPutItem(putItemEnhancedRequest).build();
        BatchWriteItemEnhancedRequest enhancedRequest = BatchWriteItemEnhancedRequest.builder().addWriteBatch(writeBatch).build();
        BatchWriteResult result = enhancedClient.batchWriteItem(enhancedRequest);
        LOGGER.info(result.toString());
    }

    public List<Submission> getSubmissionByEmail(final String email) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
        DynamoDbTable<Submission> submissionTable = enhancedClient.table(tableName, SUBMISSION_STATIC_TABLE_SCHEMA);
        PageIterable<Submission> customers = submissionTable.query(keyEqualTo(k -> k.partitionValue(email)));
        return customers.items().stream().collect(Collectors.toList());
    }

    public static final StaticTableSchema<Submission> SUBMISSION_STATIC_TABLE_SCHEMA = StaticTableSchema.builder(Submission.class)
            .newItemSupplier(Submission::new)
            .addAttribute(String.class, a -> a.name("name")
                    .getter(Submission::getName)
                    .setter(Submission::setName))
            .addAttribute(String.class, a -> a.name("email")
                    .getter(Submission::getEmail)
                    .setter(Submission::setEmail)
                    .tags(StaticAttributeTags.primaryPartitionKey()))
            .addAttribute(String.class, a -> a.name("phone")
                    .getter(Submission::getPhone)
                    .setter(Submission::setPhone))
            .addAttribute(String.class, a -> a.name("comments")
                    .getter(Submission::getComments)
                    .setter(Submission::setComments))
            .addAttribute(EnhancedType.of(OrderFormSelectionEnum.class), a -> a.name("selection")
                    .getter(Submission::getOrderFormSelectionEnum)
                    .setter(Submission::setOrderFormSelectionEnum))
            .addAttribute(EnhancedType.of(FormEnum.class), a -> a.name("form")
                    .getter(Submission::getForm)
                    .setter(Submission::setForm))
            .addAttribute(EnhancedType.of(OrganizationIdEnum.class), a -> a.name("organizationId")
                    .getter(Submission::getOrganizationId)
                    .setter(Submission::setOrganizationId))
            .addAttribute(ZonedDateTime.class, a -> a.name("createdDate")
                    .getter(Submission::getCreatedDate)
                    .setter(Submission::setCreatedDate))
            .addAttribute(ZonedDateTime.class, a -> a.name("lastUpdatedDate")
                    .getter(Submission::getLastUpdatedDate)
                    .setter(Submission::setLastUpdatedDate)
                    .tags(StaticAttributeTags.primarySortKey()))
            .addAttribute(Integer.class, a -> a.name("version")
                    .getter(Submission::getVersion)
                    .setter(Submission::setVersion))
            .build();
}
