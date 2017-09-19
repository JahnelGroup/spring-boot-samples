package com.jahnelgroup.datarestsecurity.securefield

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.annotation.CreatedBy
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils

@Component
@ConditionalOnBean(SecureFieldCreatedByAware::class)
class DefaultSecureCreatedByAware : SecureFieldCreatedByAware {

    override fun getCreatedBy(target: Any): String {
        var createdByField = target.javaClass.declaredFields.firstOrNull {
            // first try to find the annotation on the base class
            it.isAnnotationPresent(CreatedBy::class.java)
        } ?:
        target.javaClass.superclass.declaredFields.firstOrNull {
            // else check the super class fields
            it.isAnnotationPresent(CreatedBy::class.java)
        }

        // if it exists return the value
        return when( createdByField != null ){
            true -> {
                ReflectionUtils.makeAccessible(createdByField)
                createdByField?.get(target)?.toString() ?: ""
            }
            false -> ""
        }
    }

}