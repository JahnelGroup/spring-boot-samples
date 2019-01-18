package com.jahnelgroup.acl.config

import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class AclMethodSecurityConfiguration : GlobalMethodSecurityConfiguration() {

    @Autowired
    internal var defaultMethodSecurityExpressionHandler: MethodSecurityExpressionHandler? = null

    override fun createExpressionHandler(): MethodSecurityExpressionHandler? {
        return defaultMethodSecurityExpressionHandler
    }
}
