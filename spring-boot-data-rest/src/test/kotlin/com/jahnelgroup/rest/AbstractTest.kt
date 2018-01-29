package com.jahnelgroup.rest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import java.io.UnsupportedEncodingException
import org.springframework.test.web.servlet.MvcResult



@SpringBootTest(classes = arrayOf(com.jahnelgroup.rest.App::class))
@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
abstract class AbstractTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    var STEVEN_CREDENTIALS = httpBasic("steven", "password")
    var CARRIE_CREDENTIALS = httpBasic("carrie", "password")
    var LAUREN_CREDENTIALS = httpBasic("lauren", "password")

    @Throws(UnsupportedEncodingException::class)
    fun contentBody(result: MvcResult): String {
        return result.response.contentAsString
    }
}