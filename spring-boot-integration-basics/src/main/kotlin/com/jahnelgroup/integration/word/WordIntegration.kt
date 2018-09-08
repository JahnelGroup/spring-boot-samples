package com.jahnelgroup.integration.word

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlows

@Configuration
class WordIntegration {

    @Bean
    fun wordIntegrationFlow() = IntegrationFlows
            .from("wordChannel")
            .split {
                it.delimiters(" ")
            }
            .transform<String, Int> {
                it.length
            }
            .aggregate {
                it.outputProcessor {
                    it.messages.stream().mapToInt { it.payload as Int }.sum()
                }
            }
            .channel("wordChannelResult")
            .get()

}