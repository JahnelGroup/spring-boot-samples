package com.jahnelgroup.rest.user

import com.jahnelgroup.rest.AbstractTest
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest
@RunWith(SpringRunner::class)
class UserTests : AbstractTest() {

    @Test
    fun `raw user list`() {
        mockMvc.perform(get("/users"))
            .andExpect(jsonPath("$._embedded.users[*].username",
                    Matchers.contains("steven", "carrie", "lauren")))
    }

}