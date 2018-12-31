package com.jahnelgroup.acl.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import com.jahnelgroup.acl.service.context.UserContextService
import org.springframework.data.domain.AuditorAware
import com.jahnelgroup.acl.service.context.DateTimeService
import org.springframework.context.annotation.Bean
import org.springframework.data.auditing.DateTimeProvider
import java.util.*

@Configuration
@EnableJpaAuditing(
        auditorAwareRef = "userContextProvider",
        dateTimeProviderRef = "dateTimeProvider")
class JpaAuditConfig {

    @Bean
    fun dateTimeProvider(dateTimeService: DateTimeService): DateTimeProvider {
        return DateTimeProvider {
            Optional.of(dateTimeService.getInstant())
        }
    }

    @Bean
    fun userContextProvider(userContextService: UserContextService): AuditorAware<String> {
        return AuditorAware<String>{
            Optional.of(userContextService.currentUsername())
        }
    }

}