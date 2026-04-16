package com.cedarmeadowmeats.orderservice.testcontainers;

import com.cedarmeadowmeats.orderservice.repository.OrderRepository;
import io.floci.testcontainers.FlociContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import java.lang.invoke.MethodHandles;

@TestConfiguration
public class TestContainersConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final FlociContainer FLOCI = new FlociContainer();

    static {
        FLOCI.start();
        // Set endpoint before Spring creates the DynamoDbClient bean.
        System.setProperty("amazon.dynamodb.endpoint", FLOCI.getEndpoint());
    }

    @Value("${dynamodb.tableName}")
    private String tableName;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent applicationReadyEvent) {
        ApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
        DynamoDbClient client = applicationContext.getBean("dynamoDbClient", DynamoDbClient.class);
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();

        try {
            LOGGER.info("Creating table {}", tableName);
            enhancedClient.table(tableName, OrderRepository.SUBMISSION_STATIC_TABLE_SCHEMA).createTable();
        } catch (ResourceInUseException ignored) {
            LOGGER.info("Table {} already exists", tableName);
        }
    }

}
