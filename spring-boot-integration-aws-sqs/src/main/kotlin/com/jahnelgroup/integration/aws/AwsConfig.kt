package com.jahnelgroup.integration.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfig {

    /**
     * @return The AmazonSQSAsync client to be used in our com.jahnelgroup.integration
     * components to both send and receive messages using SQS
     */
    @Bean
    fun amazonSQS(): AmazonSQSAsync =
        AmazonSQSAsyncClientBuilder
                .standard()
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials()))
                .withRegion(Regions.US_EAST_2)
                .build()

    @Data
    class SpringAWSCredentials: AWSCredentials {
        var accessKey: String = ""
        var secretKey: String = ""

        override fun getAWSAccessKeyId(): String = accessKey
        override fun getAWSSecretKey(): String = secretKey
    }

    @Bean
    @ConfigurationProperties("aws.credentials")
    fun awsCredentials(): SpringAWSCredentials = SpringAWSCredentials()

}
