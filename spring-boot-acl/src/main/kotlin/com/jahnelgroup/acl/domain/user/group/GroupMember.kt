package com.jahnelgroup.acl.domain.user.group

import javax.persistence.*
import javax.validation.constraints.NotNull

@Table(name = "group_members")
@Entity
class GroupMember (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private var id: Long,

        var username: String,

        @field:NotNull
        @field:ManyToOne
        var group: Group
)