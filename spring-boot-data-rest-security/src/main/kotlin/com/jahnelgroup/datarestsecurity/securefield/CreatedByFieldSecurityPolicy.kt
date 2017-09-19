package com.jahnelgroup.datarestsecurity.securefield

import org.springframework.security.core.context.SecurityContextHolder
import java.lang.reflect.Field

class CreatedByFieldSecurityPolicy : FieldSecurityPolicy {

    override fun permitAccess(target: Any, createdBy: String?): Boolean =
        createdBy == SecurityContextHolder.getContext().authentication.name

}