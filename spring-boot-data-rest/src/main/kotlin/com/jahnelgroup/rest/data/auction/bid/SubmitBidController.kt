package com.jahnelgroup.rest.data.auction.bid

import com.jahnelgroup.rest.common.context.UserContextService
import com.jahnelgroup.rest.data.auction.Auction
import com.jahnelgroup.rest.data.auction.AuctionRepo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceProcessor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

const val SUBMIT_BID_LINK : String = "submitBid"

/**
 *
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#_programmatic_links
 */
@Configuration
@RestController
@RequestMapping("/api/auctions/{id}")
class SubmitBidController(
        private val entityLinks: RepositoryEntityLinks,
        private val userContextService: UserContextService,
        private val auctionRepo: AuctionRepo
) {

    /**
     * Using Spring Data REST's RepositoryEntityLinks we attach the links to single
     * Auction resource's.
     */
    @Bean
    fun link() = ResourceProcessor<Resource<Auction>> {
        it.apply {
            it.add(entityLinks.linkForSingleResource(it.content).slash(SUBMIT_BID_LINK).withRel(SUBMIT_BID_LINK))
        }
    }

    /**
     * Controller POST mapping to submit a Bid.
     */
    @PostMapping(value = SUBMIT_BID_LINK)
    fun submitBid(@PathVariable("id") auction: Auction?, @RequestBody bid: Bid) : Bid {

        if( auction == null ){
            throw AuctionNotFoundException()
        }

        if( !auction.isAcceptingBids() ){
            throw UnableToBidException()
        }

        // Add the bid
        auction.bids = auction.bids.plus(bid)
        bid.auction = auction
        auctionRepo.save(auction)

        return bid
    }

}

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
class UnableToBidException : RuntimeException()

@ResponseStatus(HttpStatus.NOT_FOUND)
class AuctionNotFoundException : RuntimeException()