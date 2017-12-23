package com.jahnelgroup.datarestsecurity.club

import com.fasterxml.jackson.annotation.JsonFilter
import com.jahnelgroup.datarestsecurity.audit.AbstractEntity
import com.jahnelgroup.datarestsecurity.person.Person
import com.jahnelgroup.datarestsecurity.policy.ClubFieldPolicy
import com.jahnelgroup.jackson.security.SecureField
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.OneToMany

@JsonFilter("securityFilter")
@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Club(

        var clubName : String = "",

        @SecureField(policyClasses = arrayOf(ClubFieldPolicy::class))
        var clubSecretPhrase: String = ""

) : AbstractEntity() {

    @OneToMany(cascade = arrayOf(CascadeType.ALL), orphanRemoval = true, mappedBy = "club")
    var members : List<Person> = ArrayList()

}