@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package com.jahnelgroup.acl.config.acl

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.model.PermissionGrantingStrategy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.security.acls.AclPermissionCacheOptimizer
import org.springframework.security.acls.domain.*
import org.springframework.security.acls.domain.SpringCacheBasedAclCache
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

/**
 * References:
 *      https://www.baeldung.com/spring-security-acl
 *      https://www.youtube.com/watch?v=epsRNx6PSAM&list=PLGXpHMFOMTTbCC4t6WSoKfVnUxHmyGXKJ&index=46
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class AclConfig(
        private var dataSource: DataSource
) {

    @Bean
    fun aclMethodSecurityConfig() =
            object : GlobalMethodSecurityConfiguration(){
                override fun createExpressionHandler() = defaultMethodSecurityExpressionHandler()
            }

    @Bean
    fun defaultMethodSecurityExpressionHandler() =
            DefaultMethodSecurityExpressionHandler().apply{
                setPermissionCacheOptimizer(permissionCacheOptimizer())
                setPermissionEvaluator(permissionEvaluator())
            }

    @Bean
    fun permissionCacheOptimizer() =
            AclPermissionCacheOptimizer(aclService())

    @Bean
    fun permissionEvaluator() =
            AclPermissionEvaluator(aclService())

    @Bean
    fun aclService() =
            JdbcMutableAclService(
                    dataSource, lookupStrategy(), aclCache())

    @Bean
    fun lookupStrategy() =
            BasicLookupStrategy(
                    dataSource,
                    aclCache(),
                    aclAuthStrategy(),
                    consoleAuditLogger()
            )

    @Bean
    fun consoleAuditLogger() =
            ConsoleAuditLogger()

    @Bean
    fun aclAuthStrategy() =
            AclAuthorizationStrategyImpl(adminAuthority())

    @Bean
    fun adminAuthority() =
            SimpleGrantedAuthority("ROLE_ADMIN")

    @Bean
    fun aclCache(): SpringCacheBasedAclCache {
        return SpringCacheBasedAclCache(
                aclSpringCache(),
                permissionGrantingStrategy(),
                aclAuthStrategy())
    }

    @Bean
    fun aclSpringCache(): ConcurrentMapCache {
        return ConcurrentMapCache("aclCache")
    }

    @Bean
    fun permissionGrantingStrategy(): PermissionGrantingStrategy {
        return DefaultPermissionGrantingStrategy(consoleAuditLogger())
    }

}