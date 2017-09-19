package com.jahnelgroup.datarestsecurity.person

import com.fasterxml.jackson.annotation.JsonFilter
import com.jahnelgroup.datarestsecurity.securefield.SecureField
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Entity
import javax.persistence.EntityListeners

@JsonFilter("securityFilter")
@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Person (

    //
    // Everyone can see these fields
    //
    var firstName : String = "",
    var lastName : String = "",

    //
    // Only the owner of this entity can see these fields
    //
    @SecureField
    var ssn : String = ""

) : AbstractEntity()