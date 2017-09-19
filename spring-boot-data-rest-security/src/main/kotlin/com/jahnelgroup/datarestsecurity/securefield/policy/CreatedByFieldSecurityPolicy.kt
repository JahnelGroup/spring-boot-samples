package com.jahnelgroup.datarestsecurity.securefield.policy

import com.fasterxml.jackson.databind.ser.PropertyWriter

class CreatedByFieldSecurityPolicy : FieldSecurityPolicy {

    override fun permitAccess(writer: PropertyWriter, target: Any, createdBy: String?, currentPrincipal: String?): Boolean =
        createdBy == currentPrincipal

}