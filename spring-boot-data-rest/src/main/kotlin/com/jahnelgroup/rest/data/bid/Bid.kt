package com.jahnelgroup.rest.data.bid

import com.jahnelgroup.rest.common.data.AbstractEntity
import com.jahnelgroup.rest.data.auction.Auction
import com.jahnelgroup.rest.data.user.User
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import javax.persistence.*

@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Bid (

    var amount : BigDecimal

) : AbstractEntity(){

    @OneToOne
    @JoinColumn(name = "createdBy", insertable = false, updatable = false)
    var user : User? = null

    @ManyToOne
    var auction : Auction? = null

}