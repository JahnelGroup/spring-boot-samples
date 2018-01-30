package com.jahnelgroup.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
class UnitTest {

    @Test
    fun `(Whitespace Split) Test whitespace split works`() {
        val str = "asdasdf      asdf asdf     asf \t\tasdf\nasdfaf"
        val split = str.split(Regex("\\s+"))
        assertEquals(split.size, 6)
    }

}