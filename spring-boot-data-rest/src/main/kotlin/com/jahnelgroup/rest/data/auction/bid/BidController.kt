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
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityManager

const val SUBMIT_BID_LINK : String = "submitBid"
const val CANCEL_BID_LINK : String = "cancelBid"

/**
 *
 * https://docs.spring.io/spring-data/rest/docs/current/reference/html/#_programmatic_links
 */
@Configuration
@RestController
@RequestMapping("/api/auctions/{id}")
class SubmitBidController(
        private val userContextService: UserContextService,
        private val entityLinks: RepositoryEntityLinks,
        private val auctionRepo: AuctionRepo,
        private val bidRepo: BidRepo,
        private var entityManager: EntityManager
) {

    @Bean
    fun auctionLinks() = ResourceProcessor<Resource<Auction>> {
        it.apply {
            if( it.content.isAcceptingBids() ){
                var myBid = it.content.bids.firstOrNull {
                    it.user!!.id == userContextService.getCurrentUserId()
                }

                if( myBid != null ){
                    it.add(entityLinks.linkForSingleResource(it.content).slash(CANCEL_BID_LINK).withRel(CANCEL_BID_LINK))
                }else{
                    it.add(entityLinks.linkForSingleResource(it.content).slash(SUBMIT_BID_LINK).withRel(SUBMIT_BID_LINK))
                }
            }

        }
    }

    /**
     * Submit a Bid
     */
    @PostMapping(value = SUBMIT_BID_LINK)
    fun submitBid(@PathVariable("id") auction: Auction?, @RequestBody bid: Bid) : Bid {
        if (auction == null || auction.id == null) {
            throw AuctionNotFoundException()
        }

        if (!auction.isAcceptingBids()) {
            throw UnableToBidException()
        }



        var existingBid = bidRepo.findMyBidByAuctionId(auction!!.id!!)
        if( existingBid != null ){
            // have to map values by hand...
            existingBid.amount = bid.amount
            return bidRepo.save(existingBid)
        }else {
            bid.auction = auction
            return bidRepo.save(bid)
        }
    }

    /**
     * Cancel a Bid
     */
    @DeleteMapping(value = CANCEL_BID_LINK)
    fun cancelBId(@PathVariable("id") auction: Auction?) : ResponseEntity<Any> {
        if (auction != null && auction.id != null) {
            var bid = bidRepo.findMyBidByAuctionId(auction!!.id!!)
            if( bid != null ){
                auction.removeBid(bid)
                bidRepo.delete(bid)
            }
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
class UnableToBidException : RuntimeException()

@ResponseStatus(HttpStatus.NOT_FOUND)
class AuctionNotFoundException : RuntimeException()