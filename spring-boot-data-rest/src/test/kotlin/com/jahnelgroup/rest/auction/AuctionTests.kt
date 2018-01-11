package com.jahnelgroup.rest.auction

import com.jahnelgroup.rest.AbstractTest
import com.jahnelgroup.rest.data.auction.Auction
import com.jahnelgroup.rest.data.auction.AuctionDetails
import com.jahnelgroup.rest.data.user.UserRepo
import org.hamcrest.Matchers
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@RunWith(SpringRunner::class)
class AuctionTests : AbstractTest() {

    @Autowired
    lateinit var userRepo : UserRepo

    @Ignore
    @Test
    fun `create an auction`() {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/auctions").with(STEVEN_CREDENTIALS)
            .content(objectMapper.writeValueAsBytes(Auction(
                title = "Intel CPU's",
                auctionDetails = AuctionDetails(
                        description = "Selling brand new intel CPU's, trusted seller!",
                        quantity = 10)
            ))))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        var stevenId = userRepo.findByUsername("steven").get()!!

        mockMvc.perform(MockMvcRequestBuilders.get("/users/"+stevenId+"/sellingAuctions").with(STEVEN_CREDENTIALS))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

}