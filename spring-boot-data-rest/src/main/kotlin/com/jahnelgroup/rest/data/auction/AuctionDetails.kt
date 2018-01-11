package com.jahnelgroup.rest.data.auction

import com.jahnelgroup.rest.common.AbstractEntity
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class AuctionDetails (

        @OneToOne
        var auction : Auction,

        var description : String,
        var quantity : Long

) : AbstractEntity()