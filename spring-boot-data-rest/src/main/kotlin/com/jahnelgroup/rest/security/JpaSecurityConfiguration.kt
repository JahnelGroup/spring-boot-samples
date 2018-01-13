package com.jahnelgroup.rest.security

import com.jahnelgroup.rest.common.context.UserContextService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport
import org.springframework.security.access.expression.SecurityExpressionRoot
import org.springframework.security.core.context.SecurityContextHolder

/**
 * https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions
 */
@Configuration
internal class JpaSecurityConfiguration (var userContextService: UserContextService){

    @Bean
    fun securityExtension() = object: EvaluationContextExtensionSupport() {
        override fun getExtensionId() = "security"
        override fun getRootObject() = object : SecurityExpressionRoot(SecurityContextHolder.getContext().authentication) {}
    }

    @Bean
    fun userContextExtension() = object: EvaluationContextExtensionSupport() {
        override fun getExtensionId() = "userContext"
        override fun getRootObject() = userContextService
    }
}