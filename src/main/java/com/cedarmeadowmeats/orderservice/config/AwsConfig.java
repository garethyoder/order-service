package com.cedarmeadowmeats.orderservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class AwsConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${amazon.dynamodb.endpoint:}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.access-key:}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secret-key:}")
    private String amazonAWSSecretKey;

    public AwsConfig() {
    }

    // create the dynamoDB client using the credentials and specific region
    private static final Region region = Region.US_EAST_1;

    @Bean
    DynamoDbClient dynamoDbClient() {
        DynamoDbClient dynamoDbClient;
        List<String> accessParams = Stream.of(amazonAWSAccessKey, amazonAWSSecretKey, amazonDynamoDBEndpoint)
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toList());
        if (ObjectUtils.isEmpty(accessParams)) {
            LOGGER.info("DynamoDbClient defined by IAM roles");
            dynamoDbClient = DynamoDbClient.builder()
                    .region(region)
                    .build();
        } else {
            LOGGER.info("DynamoDbClient defined by properties");
            AwsCredentialsProvider credentials = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(amazonAWSAccessKey, amazonAWSSecretKey));
            dynamoDbClient = DynamoDbClient.builder()
                    .region(region)
                    .credentialsProvider(
                            credentials)
                    .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                    .build();
        }
        return dynamoDbClient;
    }



}
