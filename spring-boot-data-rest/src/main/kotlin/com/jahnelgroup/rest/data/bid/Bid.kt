package com.jahnelgroup.rest.data.bid

import com.jahnelgroup.rest.common.data.AbstractEntity
import com.jahnelgroup.rest.data.auction.Auction
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.OneToOne

@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Bid (

    var user : String = "",

    @OneToOne
    var auction : Auction

) : AbstractEntity()