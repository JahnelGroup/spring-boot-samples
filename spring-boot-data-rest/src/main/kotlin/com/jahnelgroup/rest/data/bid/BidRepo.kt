package com.jahnelgroup.rest.data.bid

import org.springframework.data.repository.CrudRepository

interface BidRepo : CrudRepository<Bid, Long> {

    fun findBidsByUserId(userId : Long) : Set<Bid>
    fun findBidsByAuctionId(auctionId : Long) : Set<Bid>

}