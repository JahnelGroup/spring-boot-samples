package com.jahnelgroup.rest.data.auction

import com.jahnelgroup.rest.common.AbstractEntity
import com.jahnelgroup.rest.data.user.User
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
data class Auction (

        // Many Auctions can be sold by One User
        @ManyToOne
        var seller : User,

        // Many Auctions can be bid on by Many Users
        @ManyToMany
        var bidders : Set<User>,

        @OneToOne
        var auctionDetails : AuctionDetails,

        var title : String = ""

) : AbstractEntity()