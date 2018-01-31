package com.jahnelgroup.integration.message.gateway

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.channel.MessageChannels
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel

@Configuration
class SqsGatewayConfig {

    /**
     * A DirectChannel (meaning that the sender and receiver both execute in the same thread)
     * for handling inbound TextMessage requests.  This is bound to the aforementioned gateway
     * method above.
     * @return the MessageChannel named 'sqsGatewaySendChannel'
     */
    @Bean
    fun sqsGatewaySendChannel(): MessageChannel = MessageChannels.direct("sqsGatewaySendChannel").get()

    /**
     * A DirectChannel (meaning that the sender and receiver both execute in the same thread)
     * for handling GET TextMessage requests.  This is bound to the aforementioned gateway
     * method above.
     * @return the MessageChannel named 'sqsGetChannel'
     */
    @Bean
    fun sqsGetChannel(): MessageChannel = MessageChannels.direct("sqsGetChannel").get()

    /**
     * A QueueChannel that our SQS inbound channel adapter sends it's messages to.  QueueChannels
     * store the message in-memory the sender continues processing in it's own thread.  To consume
     * messages from the QueueChannel a poller must be configured on the consuming endpoint.
     * @return the PollableChannel named 'sqsQueue'
     */
    @Bean
    fun sqsQueue(): PollableChannel = MessageChannels.queue("sqsQueue").get()

}
