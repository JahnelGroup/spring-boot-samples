package com.jahnelgroup.rest.data.auction

import org.springframework.data.repository.CrudRepository

interface AuctionRepo : CrudRepository<Auction, Long>