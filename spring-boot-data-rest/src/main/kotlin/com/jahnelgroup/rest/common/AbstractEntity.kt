package com.jahnelgroup.rest.common

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.hateoas.Identifiable
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
@EntityListeners(value = AuditingEntityListener::class)
abstract class AbstractEntity : AbstractAggregateRoot(), Identifiable<Long>, Serializable {

    companion object {
        val serialVersionUId = 1L
    }

    @Id
    @GeneratedValue
    @JsonIgnore
    private var id : Long? = null

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition="TIMESTAMP")
    var createdDatetime : LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false, columnDefinition="TIMESTAMP")
    var lastModifiedDatetime : LocalDateTime = LocalDateTime.now()

    @Version
    @Column(nullable = false)
    var version : Long = 0

    override fun getId(): Long? {
        return id
    }

}