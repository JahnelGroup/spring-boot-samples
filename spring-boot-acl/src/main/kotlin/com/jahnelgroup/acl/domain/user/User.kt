package com.jahnelgroup.acl.domain.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jahnelgroup.springframework.security.acl.annotations.AclSid
import javax.persistence.*

@Table(name = "users")
@Entity
data class User (

    @field:AclSid
    @field:Id
    var username: String,

    @get:JsonIgnore
    var password: String,

    @get:JsonIgnore
    var enabled: Boolean,

    @field:OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "id.username", targetEntity = UserAuthority::class)
    var authorities: Set<UserAuthority> = emptySet()

)