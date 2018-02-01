package com.jahnelgroup.elasticsearch.integration

import com.jahnelgroup.elasticsearch.data.person.Person
import com.jahnelgroup.elasticsearch.data.person.elastic.ElasticPerson
import com.jahnelgroup.elasticsearch.data.person.elastic.PersonSearchRepo
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.event.AfterCreateEvent
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer

@Configuration
open class Integration{
    @Autowired
    lateinit var personSearchRepo: PersonSearchRepo

    @Bean
    open fun create(): ApplicationEventListeningMessageProducer {
        val producer = ApplicationEventListeningMessageProducer()
        producer.setEventTypes(AfterCreateEvent::class.java)
        return producer
    }

    @Bean
    open fun createFlow(): IntegrationFlow {
        return IntegrationFlows.from(create())
                .filter{afterCreateEvent: AfterCreateEvent -> afterCreateEvent.source is Person}
                .log()
                .handle { payload: AfterCreateEvent, _ ->
                    val elasticPerson = ElasticPerson()
                    BeanUtils.copyProperties(payload.source as Person, elasticPerson)
                    println(personSearchRepo.save(elasticPerson))
                }
                .get()

    }


}
