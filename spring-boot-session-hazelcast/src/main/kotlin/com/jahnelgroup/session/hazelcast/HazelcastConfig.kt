package com.jahnelgroup.session.hazelcast

import com.hazelcast.config.Config
import com.hazelcast.config.EntryListenerConfig
import com.hazelcast.config.MapAttributeConfig
import com.hazelcast.config.MapIndexConfig
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.hazelcast.HazelcastSessionRepository
import org.springframework.session.hazelcast.PrincipalNameExtractor
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession

@Configuration
@EnableHazelcastHttpSession
class HazelcastConfig() {

    @Bean
    fun hazelcastInstance(): HazelcastInstance {
        val attributeConfig = MapAttributeConfig()
                .setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
                .setExtractor(PrincipalNameExtractor::class.java.name)

        val config = Config()

        config.getMapConfig("spring:session:sessions")
                .addEntryListenerConfig(EntryListenerConfig(SessionListener::class.java.name, true, false))
                .addMapAttributeConfig(attributeConfig)
                .addMapIndexConfig(MapIndexConfig(
                        HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false))

        return Hazelcast.newHazelcastInstance(config)
    }

}