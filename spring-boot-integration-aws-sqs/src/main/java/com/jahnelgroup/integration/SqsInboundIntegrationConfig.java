package com.jahnelgroup.integration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aws.outbound.SqsMessageHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class SqsInboundIntegrationConfig {

    @Autowired
    private AmazonSQSAsync amazonSQS;

    @Autowired
    private MessageChannel sqsSendChannel;

    @Bean
    public SqsMessageHandler sqsMessageHandler() {
        return new SqsMessageHandler(amazonSQS);
    }

    @Bean
    public IntegrationFlow httpInboundFlow() {
        return IntegrationFlows.from(sqsSendChannel)
                .log(LoggingHandler.Level.INFO)
                .enrich(e -> e
                        .property("sentTs", LocalDateTime.now())
                        .property("uuid", UUID.randomUUID().toString()))
                .handle(sqsMessageHandler())
                .get();
    }


}
