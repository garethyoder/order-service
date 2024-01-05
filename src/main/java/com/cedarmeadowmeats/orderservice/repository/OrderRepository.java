package com.cedarmeadowmeats.orderservice.repository;

import com.cedarmeadowmeats.orderservice.model.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.lang.invoke.MethodHandles;

@Repository
public class OrderRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final DynamoDbClient dynamoDbClient;

    public OrderRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void saveSubmission(final Submission submission) {
        LOGGER.info(submission.toString());
        PutItemRequest request = PutItemRequest.builder().build();
        PutItemResponse response = dynamoDbClient.putItem(request);
        LOGGER.info(response.toString());
    }
}
