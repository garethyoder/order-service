package com.cedarmeadowmeats.orderservice.testcontainers;

import com.cedarmeadowmeats.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.lang.invoke.MethodHandles;

@TestConfiguration
@TestPropertySource(properties = {
        "amazon.aws.access-key=1",
        "amazon.aws.secret-key=2"
})
public class TestContainersConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final DockerImageName FLOCI_IMAGE_NAME = DockerImageName.parse("hectorvent/floci:latest");
    private static final int AWS_EDGE_PORT = 4566;

    @Value("${dynamodb.tableName}")
    private String tableName;

    @Bean
    public GenericContainer<?> flociContainer() {
        GenericContainer<?> floci = new GenericContainer<>(FLOCI_IMAGE_NAME)
                .withExposedPorts(AWS_EDGE_PORT)
                .withEnv("AWS_DEFAULT_REGION", "us-east-1")
                .waitingFor(Wait.forListeningPort())
                .withReuse(true);
        floci.start();
        String endpoint = String.format("http://%s:%d", floci.getHost(), floci.getMappedPort(AWS_EDGE_PORT));
        System.setProperty("amazon.dynamodb.endpoint", endpoint);
        return floci;
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent applicationReadyEvent) {
        ApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
        DynamoDbClient client = applicationContext.getBean("dynamoDbClient", DynamoDbClient.class);
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
        LOGGER.info("Creating table");
        enhancedClient.table(tableName, OrderRepository.SUBMISSION_STATIC_TABLE_SCHEMA).createTable();
    }

}
