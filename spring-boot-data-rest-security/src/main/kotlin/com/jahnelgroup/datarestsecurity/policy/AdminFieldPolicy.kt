package com.jahnelgroup.datarestsecurity.policy

import com.fasterxml.jackson.databind.ser.PropertyWriter
import com.jahnelgroup.jackson.security.SecureField
import com.jahnelgroup.jackson.security.policy.FieldSecurityPolicy
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component("adminFieldPolicy")
class AdminFieldPolicy : FieldSecurityPolicy {

    // example using SpEL
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun permitAccess(secureField: SecureField, writer: PropertyWriter, target: Any, targetCreatedByUser: String?, currentPrincipalUser: String?): Boolean {
        return true
    }

}