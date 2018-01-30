package com.jahnelgroup.integration.message.integration

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.aws.outbound.SqsMessageHandler
import org.springframework.integration.aws.support.AwsHeaders
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.support.GenericHandler
import org.springframework.integration.dsl.support.Transformers
import org.springframework.integration.handler.LoggingHandler
import org.springframework.integration.json.ObjectToJsonTransformer
import org.springframework.integration.store.MessageStore
import org.springframework.integration.store.SimpleMessageStore
import org.springframework.integration.support.json.Jackson2JsonObjectMapper
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.MessageBuilder
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityManagerFactory

/**
 * Outbound Integration Flow sending outgoing messages from this Application
 * out to SQS.
 */
@Configuration
class SqsOutboundIntegrationConfig {

    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    lateinit var amazonSQS: AmazonSQSAsync

    @Autowired
    lateinit var sqsGatewaySendChannel: MessageChannel
    
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Value("\${aws.queue}")
    lateinit var queue: String

    /**
     * The Spring Integration wrapper of ObjectMapper for serializing /
     * deserializing JSON payloads.
     * @return the Jackson2JsonObjectMapper
     */
    @Bean
    fun jackson2JsonObjectMapper(): Jackson2JsonObjectMapper = Jackson2JsonObjectMapper(objectMapper)

    /**
     * The SqsMessageHandler is a no-reply message handler.  Typically
     * these are used at the end of an com.jahnelgroup.integration flow, although they can
     * be wrapped in a reply producing message handler.  The AmazonSQSAsync client
     * is injected as the delegate for sending the message to SQS
     * @return the SqsMessageHandler
     */
    @Bean
    fun sqsMessageHandler(): SqsMessageHandler = SqsMessageHandler(amazonSQS)

    /**
     * A simple in-memory MessageStore.  This is useful when you want
     * to store a payload that is going to be altered (i.e. transformed
     * to JSON) for later use.
     * @return the MessageStore
     */
    @Bean
    fun inMemoryMessageStore(): MessageStore = SimpleMessageStore()

    /**
     * The com.jahnelgroup.integration flow handling inbound TextMessage POST requests
     * @return the IntegrationFlow 'httpInboundFlow'
     */
    @Bean
    fun sendToSqsFlow(): IntegrationFlow {
        // Start the flow from the gateway inbound channel
        return IntegrationFlows.from(sqsGatewaySendChannel)
                // Log the inbound message at INFO level
                .log(LoggingHandler.Level.INFO)
                // Store the inbound message in the in-memory message store
                // The new payload at this point is the UUID which is the key to
                // getting the message out of the message store
                .claimCheckIn(inMemoryMessageStore())
                // Store the message store key in the message headers for later use
                .enrichHeaders({ h -> h.headerFunction("messageStoreKey", { m: Message<UUID> -> m.payload.toString() }) })
                // Retrieve the original message from the store, but do not delete it
                .claimCheckOut(inMemoryMessageStore(), false)
                // Enrich some payload properties, as well as a header property
                .enrich({ e ->
                    e
                            .propertyFunction("sentToSQSTs", { _: Message<Any> -> LocalDateTime.now() })
                            .propertyFunction("uuid", { _: Message<Any> -> UUID.randomUUID().toString() })
                            .header(AwsHeaders.QUEUE, queue)
                })
                // Transform the payload to JSON
                .transform(Transformers.toJson(jackson2JsonObjectMapper(), ObjectToJsonTransformer.ResultType.STRING))
                // GenericMessageHandlers are reply producing message handlers since our gateway expects a return
                // value we must use this type of message handler or the thread will wait indefinitely (since this is
                // all synchronous) for a response.
                .handle(GenericHandler<String> { json, h ->
                    // Send the JSON message to the SqsMessageHandler
                    sqsMessageHandler().handleMessage(MessageBuilder.withPayload(json).copyHeaders(h).build())
                    // Retrieve the original Object form TextMessage from the store and return it, with the enriched properties
                    inMemoryMessageStore().removeMessage(UUID.fromString(h["messageStoreKey"].toString())).payload
                })
                .get()
    }

}
