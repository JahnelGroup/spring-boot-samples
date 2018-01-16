package com.jahnelgroup.integration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    /**
     * @return The AmazonSQSAsync client to be used in our integration
     * components to both send and receive messages using SQS
     */
    @Bean
    public AmazonSQSAsync amazonSQS() {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    @Data
    static class SpringAWSCredentials implements AWSCredentials {
        private String accessKey;
        private String secretKey;

        @Override
        public String getAWSAccessKeyId() {
            return accessKey;
        }

        @Override
        public String getAWSSecretKey() {
            return secretKey;
        }
    }

    @Bean
    @ConfigurationProperties("aws.credentials")
    public SpringAWSCredentials awsCredentials() {
        return new SpringAWSCredentials();
    }

}
