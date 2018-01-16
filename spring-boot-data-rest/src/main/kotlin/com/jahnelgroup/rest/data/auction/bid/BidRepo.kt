package com.jahnelgroup.rest.data.auction.bid

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface BidRepo : CrudRepository<Bid, Long> {

    @Query("select b from Bid b where b.auction.id = :auctionId AND b.createdBy = ?#{currentUserId}")
    fun findBidByAuctionId(@Param("auctionId") auctionId : Long) : Bid

}