package com.jahnelgroup.rest.data.auction

import com.jahnelgroup.rest.common.data.AbstractEntity
import com.jahnelgroup.rest.data.user.User
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@EntityListeners(value = AuditingEntityListener::class)
data class Auction (

        @field:Embedded
        var auctionDetails : AuctionDetails = AuctionDetails(),

        var title : String = ""

) : AbstractEntity() {

    @ManyToMany
    var bidders : Set<User> = emptySet()

}