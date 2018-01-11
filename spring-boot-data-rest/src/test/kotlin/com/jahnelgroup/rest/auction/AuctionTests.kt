package com.jahnelgroup.rest.auction

import com.jahnelgroup.rest.AbstractTest
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@RunWith(SpringRunner::class)
class AuctionTests : AbstractTest() {

    @Test
    fun `test seed users`() {
        mockMvc.perform(MockMvcRequestBuilders.post("/users"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.`is`("Steven")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.rating", Matchers.`is`(1)))
    }

}