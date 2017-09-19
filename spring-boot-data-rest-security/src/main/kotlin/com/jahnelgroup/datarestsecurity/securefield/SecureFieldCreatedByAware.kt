package com.jahnelgroup.datarestsecurity.securefield

interface SecureFieldCreatedByAware {

    fun getCreatedBy(target: Any) : String

}