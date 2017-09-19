package com.jahnelgroup.datarestsecurity.securefield

import java.lang.reflect.Field

interface FieldSecurityPolicy {

    fun permitAccess(target : Any, createdBy : String?) : Boolean

}