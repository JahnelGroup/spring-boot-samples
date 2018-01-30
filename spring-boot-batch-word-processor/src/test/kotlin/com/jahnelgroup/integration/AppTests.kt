package com.jahnelgroup.integration

import com.jahnelgroup.integration.model.WordProcessingStatsRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [App::class, AppTests.Companion.TestContext::class])
class AppTests {

    companion object {

        @Configuration
        class TestContext {

            @Bean
            fun testUtils(): JobLauncherTestUtils = JobLauncherTestUtils()

        }

    }

    @Value("\${fileName:lorem.txt}")
    lateinit var fileName: String

    @Autowired
    lateinit var testUtils: JobLauncherTestUtils

    @Autowired
    lateinit var wordProcessorJob: Job

    @Autowired
    lateinit var statsRepo: WordProcessingStatsRepository

    @Before
    fun cleanDb() {
        statsRepo.deleteAll()
    }

    @Test
    fun `(Context Loads) Validate ApplicationContext Loads`() {
        assertTrue(true)
    }

    @Test
    fun `(Job Runs Successfully) Validate job runs successfully`() {
        testUtils.job = wordProcessorJob
        val jobExec = testUtils.launchJob(JobParameters(hashMapOf(
                Pair("file", JobParameter(fileName)),
                Pair("timest", JobParameter(Date())))))
        assertEquals(jobExec.exitStatus, ExitStatus.COMPLETED)

        val stats = statsRepo.findAll()
        assertEquals(1, stats.size)
        val stat = stats[0]
        assertEquals(657, stat.wordCount)
        assertEquals(26, stat.capitalCount)
        assertEquals(1491, stat.vowelCount)
    }

    @Test
    fun `(Job Returns NO-OP) Job returns NO-OP when `() {
        testUtils.job = wordProcessorJob
        val firstExec = testUtils.launchJob(JobParameters(hashMapOf(
                Pair("file", JobParameter(fileName)),
                Pair("timest", JobParameter(Date())))))
        assertEquals(firstExec.exitStatus, ExitStatus.COMPLETED)

        val secExec = testUtils.launchJob(JobParameters(hashMapOf(
                Pair("file", JobParameter(fileName)),
                Pair("timest", JobParameter(Date())))))
        assertEquals(secExec.exitStatus.exitCode, ExitStatus.NOOP.exitCode)
    }

    @Test
    fun `(Job Runs Successfully) Validate job runs successfully for weird formatting`() {
        testUtils.job = wordProcessorJob
        val jobExec = testUtils.launchJob(JobParameters(hashMapOf(
                Pair("file", JobParameter("short-story.txt")),
                Pair("timest", JobParameter(Date())))))
        assertEquals(jobExec.exitStatus, ExitStatus.COMPLETED)

        val stats = statsRepo.findAll()
        assertEquals(1, stats.size)
        val stat = stats[0]
        assertEquals(3463, stat.wordCount)
        assertEquals(404, stat.capitalCount)
        assertEquals(5402, stat.vowelCount)
    }

}
