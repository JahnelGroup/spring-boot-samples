package com.jahnelgroup.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
class IntegrationApplicationTests {

	@Test
	fun contextLoads() {
		println("ABC".split(Regex("")).size)
	}

}
