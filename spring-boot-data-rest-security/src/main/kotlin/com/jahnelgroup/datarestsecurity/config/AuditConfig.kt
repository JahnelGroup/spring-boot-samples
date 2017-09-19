package com.jahnelgroup.datarestsecurity.config

import com.jahnelgroup.datarestsecurity.securefield.FieldSecurityPolicy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
class AuditConfig {

    @Bean
    fun auditorAware() = AuditorAware<String> {
        SecurityContextHolder.getContext().authentication.name
    }

}