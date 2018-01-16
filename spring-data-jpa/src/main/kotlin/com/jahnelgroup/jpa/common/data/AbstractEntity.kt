package com.jahnelgroup.rest.common.data

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(value = AuditingEntityListener::class)
abstract class AbstractEntity : AbstractAggregateRoot(), Serializable {

    companion object {
        val serialVersionUId = 1L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long? = null

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: Long = 0

//    @CreatedDate
//    @Column(nullable = false, updatable = false, columnDefinition="TIMESTAMP")
//    var createdDatetime : ZonedDateTime = ZonedDateTime.now()

    @LastModifiedBy
    @Column(nullable = false)
    var lastModifiedBy: Long = 0

//    @LastModifiedDate
//    @Column(nullable = false, columnDefinition="TIMESTAMP")
//    var lastModifiedDatetime : ZonedDateTime = ZonedDateTime.now()

    @Version
    @Column(nullable = false)
    var version : Long = 0

    @Column(nullable = false)
    var deleted: Boolean = false

}