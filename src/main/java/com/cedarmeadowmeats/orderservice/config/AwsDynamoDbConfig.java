package com.cedarmeadowmeats.orderservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.lang.invoke.MethodHandles;
import java.net.URI;

@Configuration
public class AwsDynamoDbConfig extends AwsBaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    // create the dynamoDB client using the credentials and specific region
    private static final Region region = Region.US_EAST_1;
    @Value("${amazon.dynamodb.endpoint:}")
    private String amazonDynamoDbEndpoint;

    public AwsDynamoDbConfig() {
    }

    @Bean
    DynamoDbClient dynamoDbClient() {
        DynamoDbClient dynamoDbClient;
        if (isStaticCredentials(amazonAwsAccessKey, amazonAwsSecretKey, amazonDynamoDbEndpoint)) {
            LOGGER.info("DynamoDbClient defined by properties");
            AwsCredentialsProvider credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(amazonAwsAccessKey, amazonAwsSecretKey));
            dynamoDbClient = DynamoDbClient.builder().region(region).credentialsProvider(credentials).endpointOverride(URI.create(amazonDynamoDbEndpoint)).build();
        } else {
            LOGGER.info("DynamoDbClient defined by IAM roles");
            dynamoDbClient = DynamoDbClient.builder().region(region).build();
        }
        return dynamoDbClient;
    }

}
