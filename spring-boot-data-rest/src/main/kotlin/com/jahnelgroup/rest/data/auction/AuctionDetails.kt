package com.jahnelgroup.rest.data.auction

import javax.persistence.Embeddable

@Embeddable
data class AuctionDetails (

        var description : String = "",
        var quantity : Long = 0

)