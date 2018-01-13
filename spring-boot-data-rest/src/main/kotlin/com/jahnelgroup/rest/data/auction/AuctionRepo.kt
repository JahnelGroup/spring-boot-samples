package com.jahnelgroup.rest.data.auction

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface AuctionRepo : CrudRepository<Auction, Long>{

    /**
     * This provides an alternative to approach to getting your auctions. You could
     * have gotten it through /api/users/<id>/sellingAuctions but then you'd have
     * know your userId.
     *
     * TODO: This returns a 404, to be consistent it would be better for it to return
     * and empty list (same as if you hit sellingAuctions without have any)
     */
    @Query("select a from Auction a where a.createdBy = ?#{currentUserId}")
    fun findMyAuctions(): Optional<Set<Auction>>

}