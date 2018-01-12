package com.jahnelgroup.rest.user

import com.jahnelgroup.rest.AbstractTest
import org.junit.Test
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthenticationTests : AbstractTest() {

    @Test
    fun `Deny with no credentials`() {
        mockMvc.perform(get("/api/users")).andExpect(status().isUnauthorized)
    }

    @Test
    fun `Deny with bad password`() {
        mockMvc.perform(get("/api/users").with(httpBasic("steven", "BAD")))
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "BAD_USER", password = "password")
    fun `Deny with bad username`() {
        mockMvc.perform(get("/api/users").with(httpBasic("BAD", "password")))
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(username = "steven", password = "password")
    fun `Permit with good credentials`() {
        mockMvc.perform(get("/api/users").with(httpBasic("steven", "password")))
                .andExpect(status().isOk)
    }

}