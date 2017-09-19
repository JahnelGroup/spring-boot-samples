package com.jahnelgroup.datarestsecurity.securefield.policy

import com.fasterxml.jackson.databind.ser.PropertyWriter

interface FieldSecurityPolicy {

    fun permitAccess(writer: PropertyWriter, target : Any, createdBy : String?, currentPrincpal : String?) : Boolean

}