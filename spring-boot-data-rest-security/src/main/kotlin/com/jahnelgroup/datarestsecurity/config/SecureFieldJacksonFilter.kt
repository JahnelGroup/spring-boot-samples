package com.jahnelgroup.datarestsecurity.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.PropertyWriter
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.jahnelgroup.datarestsecurity.person.AbstractEntity
import org.springframework.security.core.context.SecurityContextHolder

class SecureFieldJacksonFilter : SimpleBeanPropertyFilter(){

    override fun serializeAsField(pojo: Any, jgen: JsonGenerator, provider: SerializerProvider, writer: PropertyWriter) {
        val secureField : SecureField? = writer.findAnnotation(SecureField::class.java)
        if( secureField != null && pojo is AbstractEntity ){
            if( pojo.createdBy == SecurityContextHolder.getContext().authentication.name ){
                writer.serializeAsField(pojo, jgen, provider)
            }
        }

        // not protected, just write it
        else{
            writer.serializeAsField(pojo, jgen, provider)
        }
    }

}