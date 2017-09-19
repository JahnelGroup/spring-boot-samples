package com.jahnelgroup.datarestsecurity.securefield

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.PropertyWriter
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import org.springframework.stereotype.Component

@Component
class SecureFieldJacksonFilter(

    private val secureCreatedByAware : SecureFieldCreatedByAware

) : SimpleBeanPropertyFilter(){

    override fun serializeAsField(pojo: Any, jgen: JsonGenerator, provider: SerializerProvider, writer: PropertyWriter) {
        val secureField : SecureField? = writer.findAnnotation(SecureField::class.java)
        val createdBy : String? = secureCreatedByAware.getCreatedBy(pojo)

        if ( secureField != null ) {
            var permit : Boolean = false

            secureField.policies.forEach {
                permit = it::javaObjectType.get().newInstance().permitAccess(pojo, createdBy)
            }

            if( permit ){
                writer.serializeAsField(pojo, jgen, provider)
            }

        }

        // not protected, just write it
        else{
            writer.serializeAsField(pojo, jgen, provider)
        }
    }

}