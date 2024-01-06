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
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.lang.invoke.MethodHandles;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

@TestConfiguration
@TestPropertySource(properties = {
        "amazon.aws.accesskey=1",
        "amazon.aws.secretkey=2"
})
public class TestContainersConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack:3.0.2");

    @Value("${dynamodb.tableName}")
    private String tableName;

    @Bean
    public LocalStackContainer localStackContainer() {
        LocalStackContainer lc = new LocalStackContainer(LOCALSTACK_IMAGE_NAME)
                .withServices(DYNAMODB)
                .withNetworkAliases("localstack")
                .withReuse(true);
        lc.start();
        System.setProperty("amazon.dynamodb.endpoint", lc.getEndpointOverride(DYNAMODB).toString());
        return lc;
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
