package com.jahnelgroup.acl.domain.user.group

import javax.persistence.*

@Table(name = "groups")
@Entity
data class Group (

        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        private var id: Long? = null,

        var groupName: String,

        @field:OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "id.groupId", targetEntity = GroupAuthority::class)
        var authorities: MutableSet<GroupAuthority> = mutableSetOf(),

        @field:OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true, mappedBy = "group")
        var members: Set<GroupMember> = emptySet()
)