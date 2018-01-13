package com.jahnelgroup.rest.data.auction.bid

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jahnelgroup.rest.common.data.AbstractEntity
import com.jahnelgroup.rest.data.auction.Auction
import com.jahnelgroup.rest.data.user.User
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import javax.persistence.*

@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Bid (

    var amount : BigDecimal = BigDecimal("100.00")

) : AbstractEntity(){

    @OneToOne
    @JoinColumn(name = "createdBy", insertable = false, updatable = false)
    var user : User? = null

    @ManyToOne
    @JsonIgnore
    var auction : Auction? = null

}