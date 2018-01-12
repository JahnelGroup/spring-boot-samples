package com.jahnelgroup.rest.auction

import com.jahnelgroup.rest.AbstractTest
import com.jahnelgroup.rest.data.auction.Auction
import com.jahnelgroup.rest.data.auction.AuctionDetails
import com.jahnelgroup.rest.data.user.UserRepo
import com.jayway.jsonpath.JsonPath
import org.hamcrest.Matchers
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class AuctionTests : AbstractTest() {

    @Autowired
    lateinit var userRepo : UserRepo

    @Test
    fun `create an auction`() {
        var body = contentBody(mockMvc.perform(MockMvcRequestBuilders.get("/api/users").with(STEVEN_CREDENTIALS))
                .andReturn())

        JsonPath.read<Long>(body, "")

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/auctions").with(STEVEN_CREDENTIALS)
            .content(objectMapper.writeValueAsBytes(Auction(
                title = "Intel CPU's",
                auctionDetails = AuctionDetails(
                        description = "Selling brand new intel CPU's, trusted seller!",
                        quantity = 10)
            ))))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

//        mockMvc.perform(MockMvcRequestBuilders.get("/users/"+stevenId+"/sellingAuctions").with(STEVEN_CREDENTIALS))
//                .andExpect(MockMvcResultMatchers.status().isOk)
    }

}