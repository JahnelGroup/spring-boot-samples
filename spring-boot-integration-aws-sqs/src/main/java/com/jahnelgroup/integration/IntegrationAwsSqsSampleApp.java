package com.jahnelgroup.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@SpringBootApplication
public class IntegrationAwsSqsSampleApp {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationAwsSqsSampleApp.class, args);
    }

    /**
     * The MessagingGateway that represents the entrypoint
     * into our integration flows.
     */
    @MessagingGateway
    public interface SqsGateway {

        /**
         * This method accepts a text message, wraps it in a Message
         * and sends it on the 'sqsGatewaySendChannel' which
         * begins the integration flow
         * @param textMessage the payload to send
         * @return the result of the integration flow
         */
        @Gateway(requestChannel = "sqsGatewaySendChannel")
        TextMessage sendMessage(TextMessage textMessage);

        /**
         * A simple RESTful GET operation implemented using
         * Spring Integration
         * @param uuid the @Id of the TextMessage
         * @return The appropriate TextMessage
         */
        @Gateway(requestChannel = "sqsGetChannel")
        TextMessage getMessage(String uuid);

    }

    /**
     * A DirectChannel (meaning that the sender and receiver both execute in the same thread)
     * for handling inbound TextMessage requests.  This is bound to the aforementioned gateway
     * method above.
     * @return the MessageChannel named 'sqsGatewaySendChannel'
     */
    @Bean
    public MessageChannel sqsGatewaySendChannel() {
        return MessageChannels.direct("sqsGatewaySendChannel").get();
    }

    /**
     * A DirectChannel (meaning that the sender and receiver both execute in the same thread)
     * for handling GET TextMessage requests.  This is bound to the aforementioned gateway
     * method above.
     * @return the MessageChannel named 'sqsGetChannel'
     */
    @Bean
    public MessageChannel sqsGetChannel() {
        return MessageChannels.direct("sqsGetChannel").get();
    }

    /**
     * A QueueChannel that our SQS inbound channel adapter sends it's messages to.  QueueChannels
     * store the message in-memory; the sender continues processing in it's own thread.  To consume
     * messages from the QueueChannel a poller must be configured on the consuming endpoint.
     * @return the PollableChannel named 'sqsQueue'
     */
    @Bean
    public PollableChannel sqsQueue() {
        return MessageChannels.queue("sqsQueue").get();
    }

}
