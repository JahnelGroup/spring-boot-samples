package com.jahnelgroup.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
@EnableAsync
@RestController
public class IntegrationAwsSqsSampleApp {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationAwsSqsSampleApp.class, args);
    }

    @MessagingGateway
    public interface SqsInboundGateway {

        @Gateway(requestChannel = "sqsSendChannel", replyChannel = "sqsReplyChannel", replyTimeout = 20000)
        @Async
        Future<TextMessage> sendMessage(TextMessage textMessage);

    }

    @Bean
    public MessageChannel sqsSendChannel() {
        return MessageChannels.executor("sqsSendChannel", Executors.newCachedThreadPool()).get();
    }

    @Bean
    public MessageChannel sqsReplyChannel() {
        return MessageChannels.direct("sqsReplyChannel").get();
    }

}
