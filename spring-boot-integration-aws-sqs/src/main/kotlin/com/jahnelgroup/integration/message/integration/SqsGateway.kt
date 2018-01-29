package com.jahnelgroup.integration.message.integration

import com.jahnelgroup.integration.message.TextMessage
import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway

@MessagingGateway
interface SqsGateway {

    /**
     * This method accepts a text message, wraps it in a Message
     * and sends it on the 'sqsGatewaySendChannel' which
     * begins the com.jahnelgroup.integration flow
     * @param textMessage the payload to send
     * @return the result of the com.jahnelgroup.integration flow
     */
    @Gateway(requestChannel = "sqsGatewaySendChannel")
    fun sendMessage(textMessage: TextMessage): TextMessage

    /**
     * A simple RESTful GET operation implemented using
     * Spring Integration
     * @param uuid the @Id of the TextMessage
     * @return The appropriate TextMessage
     */
    @Gateway(requestChannel = "sqsGetChannel")
    fun getMessage(uuid: String): TextMessage

}