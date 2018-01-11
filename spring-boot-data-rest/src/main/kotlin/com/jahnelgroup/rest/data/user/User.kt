package com.jahnelgroup.rest.data.user

import com.jahnelgroup.rest.common.AbstractEntity
import com.jahnelgroup.rest.data.auction.Auction
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
data class User (

        // One User can be selling ma
        @OneToMany
        var sellingAuctions : Set<Auction> = emptySet(),

        @OneToMany
        var biddingAuctions : Set<Auction> = emptySet(),

        var username: String = "",

        var rating : Short = 0

) : AbstractEntity()