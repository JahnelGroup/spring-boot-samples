package com.jahnelgroup.integration.message.integration;

import com.jahnelgroup.integration.message.TextMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

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