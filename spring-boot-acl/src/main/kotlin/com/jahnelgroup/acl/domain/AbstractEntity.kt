package com.jahnelgroup.acl.domain

import com.jahnelgroup.springframework.security.acl.annotations.AclObjectId
import org.springframework.data.annotation.*
import org.springframework.data.annotation.Version
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.*
import javax.persistence.Id

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AclObjectId
    var id: Long? = null

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: String = ""

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdDatetime: Instant = Instant.now()

    @LastModifiedBy
    @Column(nullable = false)
    var lastModifiedBy: String = ""

    @LastModifiedDate
    @Column(nullable = false)
    var lastModifiedDatetime: Instant = Instant.now()

    @Version
    @Column(nullable = false)
    var version: Long = 0

}