package com.jahnelgroup.acl.domain.user


import java.io.Serializable
import javax.persistence.*

@Embeddable
data class AuthorityId(
    var username: String = "",
    var authority: String = ""
) : Serializable

@Table(name = "authorities")
@Entity
data class UserAuthority(
    @EmbeddedId
    var id: AuthorityId? = null
)
