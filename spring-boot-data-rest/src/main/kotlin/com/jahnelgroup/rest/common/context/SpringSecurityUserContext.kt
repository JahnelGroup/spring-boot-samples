package com.jahnelgroup.rest.common.context

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SpringSecurityUserContext : UserContextService {

    override fun getCurrentUser() : String = SecurityContextHolder.getContext().authentication.name

}