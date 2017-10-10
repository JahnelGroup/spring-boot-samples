package com.jahnelgroup.datarestsecurity.person

import com.fasterxml.jackson.annotation.JsonFilter
import com.jahnelgroup.datarestsecurity.audit.AbstractEntity
import com.jahnelgroup.datarestsecurity.club.Club
import com.jahnelgroup.jackson.security.SecureField
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.ManyToOne

@JsonFilter("securityFilter")
@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Person (

        var firstName : String = "",
        var lastName : String = "",

        // Only the owner of this entity can see this field
        @SecureField
        var ssn : String = ""

) : AbstractEntity(){

    @ManyToOne
    var club : Club? = null

}