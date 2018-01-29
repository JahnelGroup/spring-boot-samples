package com.jahnelgroup.integration

import com.jahnelgroup.integration.message.TextMessage
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.messaging.MessageChannel
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import java.util.concurrent.Semaphore

@RunWith(SpringRunner::class)
@SpringBootTest(
        classes = [App::class, AppTests.Companion.TestConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTests {

    companion object {

        @Configuration
        class TestConfig {

            @Autowired
            lateinit var sqsInboundPubSubChannel: MessageChannel

            @Bean
            fun testSemaphore(): Semaphore = Semaphore(0)

            @Bean
            fun testPubSubFlow(): IntegrationFlow =
                    IntegrationFlows.from(sqsInboundPubSubChannel)
                            .handle({ _ -> testSemaphore().release(1) })
                            .get()
        }

    }


    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var testSemaphore: Semaphore

    @Before
    fun drainPermits() {
        testSemaphore.drainPermits()
    }

    @Test
    fun `(AWS SQS Send Receive Async) Sends HTTP REST request and receives response asynchronously`() {
        val msg = TextMessage.fromContent("Test message.")
        val responseEntity = restTemplate.postForEntity("/textMessages", msg, TextMessage::class.java)
        assertEquals(responseEntity.statusCode, HttpStatus.ACCEPTED)
        val resultMsg = responseEntity.body
        assertEquals(msg.content, resultMsg.content)
        assertNotNull(resultMsg.uuid)
        assertNotNull(resultMsg.sentToSQSTs)
        testSemaphore.acquire(1)
        val persistedEntity = restTemplate.getForEntity(
                String.format("/textMessages/%s", resultMsg.uuid),
                TextMessage::class.java)
        assertEquals(persistedEntity.statusCode, HttpStatus.OK)
        val entity = persistedEntity.body
        assertEquals(resultMsg.uuid, entity.uuid)
        assertNotNull(entity.receivedFromSQSTs)
    }

    @Test
    fun `(AWS SQS Multiple Sends) Multiple POSTs generate unique UUIDs and correct timestamps`() {
        val msg = TextMessage.fromContent("Test message.")
        val responseEntity = restTemplate.postForEntity("/textMessages", msg, TextMessage::class.java)
        assertEquals(responseEntity.statusCode, HttpStatus.ACCEPTED)

        val secondMsg = TextMessage.fromContent("Test message two.")
        val secondResponseEntity = restTemplate.postForEntity("/textMessages", secondMsg, TextMessage::class.java)
        assertEquals(secondResponseEntity.statusCode, HttpStatus.ACCEPTED)
        testSemaphore.acquire(2)

        val firstMessage = restTemplate.getForEntity(
                    String.format("/textMessages/%s", responseEntity.body.uuid),
                    TextMessage::class.java)
                .body

        val secondMessage = restTemplate.getForEntity(
                    String.format("/textMessages/%s", secondResponseEntity.body.uuid),
                    TextMessage::class.java)
                .body

        assertNotSame(firstMessage.uuid, secondMessage.uuid)
        assertNotSame(firstMessage.sentToSQSTs, secondMessage.sentToSQSTs)
        assertNotSame(firstMessage.receivedFromSQSTs, secondMessage.receivedFromSQSTs)
    }

    @Test
    fun `(AWS SQS GET 404) GET request appropriately returns 404 when not found`() {
        val response = restTemplate.getForEntity(
                String.format("/textMessages/%s", UUID.randomUUID().toString()),
                TextMessage::class.java)
        assertEquals(response.statusCode, HttpStatus.NOT_FOUND)
    }

}
