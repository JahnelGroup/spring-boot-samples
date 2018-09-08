package com.jahnelgroup.integration.alphabet

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.splitter.AbstractMessageSplitter
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHandler
import java.util.stream.Collectors

@Configuration
class AlphabetIntegration {

    @Bean
    fun resequenceFlow() = IntegrationFlows
            .from("resequenceChannel")
            .split(object : AbstractMessageSplitter(){
                override fun splitMessage(message: Message<*>) =
                        (message.payload as String).split(Regex(""))
            })
            .filter{ it: Message<String> -> it.payload.isNotBlank() }
            .resequence {
                it.correlationStrategy {  }
                it.releaseStrategy {
                    it.messages.stream().map { it.payload }.sorted().collect(Collectors.toList())
                }
            }
            //.channel("resequenceResultChannel")
            .handle(MessageHandler {
                println(it.payload)
            })
            .get()

}