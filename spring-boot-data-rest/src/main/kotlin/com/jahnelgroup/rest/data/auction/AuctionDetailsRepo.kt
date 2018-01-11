package com.jahnelgroup.rest.data.auction

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(exported = false)
interface AuctionDetailsRepo : CrudRepository<AuctionDetails, Long>