package com.jahnelgroup.acl.domain.user.group

import java.io.Serializable
import javax.persistence.*

@Embeddable
data class AuthorityId(
        var groupId: Long = 0,
        var authority: String = ""
) : Serializable

@Table(name = "group_authorities")
@Entity
data class GroupAuthority(
        @EmbeddedId
        var id: AuthorityId? = null
)