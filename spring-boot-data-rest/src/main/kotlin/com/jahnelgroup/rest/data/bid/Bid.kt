package com.jahnelgroup.rest.data.bid

import com.jahnelgroup.rest.common.AbstractEntity
import com.jahnelgroup.rest.data.auction.Auction
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class Bid (

    var user : String = "",

    @OneToOne
    var auction : Auction

) : AbstractEntity()