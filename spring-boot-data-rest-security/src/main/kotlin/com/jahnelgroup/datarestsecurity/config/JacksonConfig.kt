package com.jahnelgroup.datarestsecurity.config

import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()

        builder.filters(
            SimpleFilterProvider().addFilter("securityFilter", SecureFieldJacksonFilter())
        )

        return builder
    }

}