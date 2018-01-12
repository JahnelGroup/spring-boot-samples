package com.jahnelgroup.rest.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.core.context.SecurityContextHolder

/**
 * https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions
 */
@Configuration
//@EnableJpaRepositories
internal class JpaSecurityConfiguration {

    @Bean
    fun securityExtension() = object: EvaluationContextExtensionSupport() {
        override fun getExtensionId() = "security"
        override fun getRootObject() = object : SecurityExpressionRoot(SecurityContextHolder.getContext().authentication) {}
    }
}