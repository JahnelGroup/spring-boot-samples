package com.jahnelgroup.rest.data.auction.bid

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface BidRepo : CrudRepository<Bid, Long> {

}