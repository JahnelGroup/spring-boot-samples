package com.jahnelgroup.datarestsecurity.club

import com.fasterxml.jackson.annotation.JsonFilter
import com.jahnelgroup.datarestsecurity.audit.AbstractEntity
import com.jahnelgroup.datarestsecurity.person.Person
import com.jahnelgroup.jackson.security.SecureField

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@JsonFilter("securityFilter")
@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Club(

        var clubName : String = "",

        // only club members of this club can see this
        @SecureField(policies = arrayOf(ClubFieldPolicy::class) )
        var clubSecretPhrase: String = ""

) : AbstractEntity() {

    @OneToMany(cascade = arrayOf(CascadeType.ALL), orphanRemoval = true, mappedBy = "club")
    var members : List<Person> = ArrayList()

}