package com.jahnelgroup.integration.message.integration

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.jahnelgroup.integration.message.TextMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.aws.inbound.SqsMessageDrivenChannelAdapter
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.integration.dsl.GenericEndpointSpec
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.core.Pollers
import org.springframework.integration.dsl.jpa.Jpa
import org.springframework.integration.dsl.support.Consumer
import org.springframework.integration.dsl.support.Transformers
import org.springframework.integration.handler.LoggingHandler
import org.springframework.integration.jpa.support.PersistMode
import org.springframework.integration.support.json.Jackson2JsonObjectMapper
import org.springframework.integration.transformer.MessageTransformingHandler
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.PollableChannel
import java.time.LocalDateTime
import javax.persistence.EntityManagerFactory

/**
 * Outbound Integration Flow polling for outgoing
 * messages from SQS and into this Application.
 */
@Configuration
class SqsOutboundIntegrationConfig {

    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    lateinit var sqsQueue: PollableChannel

    @Autowired
    lateinit var amazonSQS: AmazonSQSAsync

    @Autowired
    lateinit var jackson2JsonObjectMapper: Jackson2JsonObjectMapper

    @Value("\${aws.queue}")
    lateinit var queue: String

    /**
     * The SqsMessageDrivenChannelAdapter - this will consume messages off of the specified AWS SQS
     * queue and send them on the configured message channel.  In this case, a QueueChannel which must
     * then be polled.
     * @return the SqsMessageDrivenChannelAdapter
     */
    @Bean
    fun sqsMessageDrivenChannelAdapter(): SqsMessageDrivenChannelAdapter {
        val adapter = SqsMessageDrivenChannelAdapter(amazonSQS, queue)
        adapter.outputChannel = sqsQueue
        return adapter
    }

    /**
     * A synchronous pub-sub channel.  Since no task executor has been set for the pub-sub
     * channel all subscribers will execute synchronously one at a time.
     * @return the pub-sub channel
     */
    @Bean
    fun sqsOutboundPubSubChannel(): MessageChannel = PublishSubscribeChannel()

    /**
     * The IntegrationFlow that consumes messages from SQS and publishes them on a pub-sub channel
     * @return the IntegrationFlow 'sqsOutputFlow'
     */
    @Bean
    fun sqsOutboundFlow(): IntegrationFlow =
        // Start the com.jahnelgroup.integration flow at the channel the sqs adapter is putting the messages on
        IntegrationFlows.from(sqsQueue)
                // Transform the JSON payload to our TextMessage Object
                .transform(Transformers.fromJson(TextMessage::class.java, jackson2JsonObjectMapper),
                        // This is the first endpoint consuming from the queue, so it must
                        // be configured with a poller to initiate the message flow
                        Consumer<GenericEndpointSpec<MessageTransformingHandler>> { e -> e.poller(Pollers.fixedRate(50).maxMessagesPerPoll(1)) })
                // Enrich a property of the payload
                .enrich({ e -> e.propertyFunction("receivedFromSQSTs", { _: Message<Any> -> LocalDateTime.now() }) })
                // Log the message at INFO level
                .log(LoggingHandler.Level.INFO)
                // Send the message on a pub-sub channel for any subscribers who care to consume
                .channel(sqsOutboundPubSubChannel())
                .get()

    @Bean
    fun sqsPersistenceFlow(): IntegrationFlow =
        // Subscribe the the pub-sub channel
        IntegrationFlows.from(sqsOutboundPubSubChannel())
                // Persist the TextMessage using a Jpa.outboundAdapter.  Note
                // that outbound adapters do NOT produce a reply so therefore this is
                // the end of the message flow.  If we wanted to continue processing
                // the result we would use the updatingGateway instead.  Gateways
                // produce replies.
                .handle(Jpa.outboundAdapter(entityManagerFactory)
                                .entityClass(TextMessage::class.java)
                                .persistMode(PersistMode.PERSIST)
                                .flush(true),
                        // We need to make this endpoint transactional
                        // since it is persisting data to the database
                        { e -> e.transactional() })
                .get()

}
