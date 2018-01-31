package com.jahnelgroup.integration.message.integration

import com.jahnelgroup.integration.common.ResourceNotFoundException
import com.jahnelgroup.integration.message.TextMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.support.GenericHandler
import org.springframework.messaging.MessageChannel

import java.util.Optional

/**
 * Integration Flow showcasing how you can perform simple operations
 * like retrieving an item from the database.
 */
@Configuration
class TextMessageRepoIntegrationConfig {

    @Autowired
    lateinit var sqsGetChannel: MessageChannel

    @Autowired
    lateinit var textMessageRepository: TextMessageRepository

    @Bean
    fun messageGetFlow(): IntegrationFlow =
            IntegrationFlows.from(sqsGetChannel)
                .handle(GenericHandler<String> { uuid, _ -> Optional
                        .ofNullable(textMessageRepository
                        .findOne(uuid))
                        .orElseThrow({ResourceNotFoundException()})})
                .get()

}
