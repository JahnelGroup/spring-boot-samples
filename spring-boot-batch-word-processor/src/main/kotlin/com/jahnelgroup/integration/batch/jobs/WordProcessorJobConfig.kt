package com.jahnelgroup.integration.batch.jobs

import com.jahnelgroup.integration.model.WordProcessingStats
import com.jahnelgroup.integration.model.WordProcessingStatsRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.core.job.flow.FlowExecutionStatus
import org.springframework.batch.core.job.flow.JobExecutionDecider
import org.springframework.batch.core.job.flow.support.SimpleFlow
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.util.DigestUtils
import java.util.stream.Collectors

@Configuration
class WordProcessorJobConfig {

    @Autowired
    lateinit var jobBuilder: JobBuilderFactory

    @Autowired
    lateinit var stepBuilder: StepBuilderFactory

    @Autowired
    lateinit var statsRepo: WordProcessingStatsRepository

    /**
     * Bean definition for the Job built using the JobBuilderFactory
     */
    @Bean
    fun wordProcessorJob(jobControlFlow: Flow): Job =
            // Always keep job names and step names unique, try to match the bean method name
            jobBuilder.get("wordProcessorJob")
                    // Start the job with our control flow
                    .start(jobControlFlow)
                    // Build the flow
                    .build()
                    // Build the job
                    .build()

    /**
     * The JobExecutionDecider that inspects the incoming file to determine if there is work
     * to do.  If we already have the MD5 (Hex) hash in the DB then we already processed
     * this file so exit the job with NOOP status.  Otherwise, continue.
     */
    @Bean
    fun jobExecutionDecider(): JobExecutionDecider {
        return JobExecutionDecider { jobExec, _ ->
            val file = ClassPathResource(jobExec.jobParameters.getString("file"))
            val md5hex = DigestUtils.md5DigestAsHex(file.inputStream)
            val existing = statsRepo.findOne(md5hex)
            if(existing == null) {
                jobExec.executionContext.putString("md5hex", md5hex)
                val newStats = WordProcessingStats()
                newStats.id = md5hex
                statsRepo.save(newStats)
                FlowExecutionStatus("CONTINUE")
            } else {
                FlowExecutionStatus("NO-OP")
            }
        }
    }

    /**
     * The main job control flow @Bean definition built
     * using a FlowBuilder.
     */
    @Bean
    fun jobControlFlow(wordProcessorStep: Step): Flow =
        FlowBuilder<SimpleFlow>("jobControlFlow")
                // Start off with our decided
                .start(jobExecutionDecider())
                // If it's a NOOP then end the job
                .on("NO-OP").end()
                // Otherwise continue to our normal step
                .on("CONTINUE").to(wordProcessorStep).end()

    /**
     * The main processing step in the form of a Reader -> Processor -> Writer
     * Built as a @Bean using StepBuilderFactory
     */
    @Bean
    fun wordProcessorStep(resultWriter: ItemWriter<WordProcessingStats>, inputReader: FlatFileItemReader<String>): Step =
            stepBuilder.get("wordProcessorStep")
                    // Our inbound data is in the form of Strings which we
                    // are transforming to WordProcessingStats.  The chunkSize
                    // indicates how many records we process before committing
                    // a single transaction
                    .chunk<String, WordProcessingStats>(100)
                    .reader(inputReader)
                    .processor(wordProcessor())
                    .writer(resultWriter)
                    .build()

    /**
     * The ItemReader bean using Spring's FlatFileItemReader.  Configured to take
     * a file that is on the classpath as the input resource using UTF-8 encoding.
     * It will read the file line by line and pass those lines to our processor.
     *
     * @param file: String - this is a late-binding param in Spring Batch, meaning that
     * given the scope context defined on the bean it can access job-level or step-level
     * details using @Value.  In this case, we are using @JobScope to provide access to
     * the job parameters in the bean definition.  @JobScoped beans are created per job execution.
     * @StepScoped beans are important when partitioning and accessing step level metadata.
     */
    @Bean
    @JobScope
    fun inputReader(@Value("#{jobParameters[file]}") file: String): FlatFileItemReader<String> {
        val reader: FlatFileItemReader<String> = FlatFileItemReader()
        reader.setEncoding("UTF-8")
        reader.setLineMapper({ line, _ -> line })
        reader.setStrict(true)
        reader.setResource(ClassPathResource(file))
        return reader
    }

    /**
     * ItemProcessor to process a line transforming it into a WordProcessingStats object
     * which encapsulates the counts we are interested in.
     */
    @Bean
    fun wordProcessor(): ItemProcessor<String, WordProcessingStats> {
        return ItemProcessor {
            val stats = WordProcessingStats()
            // Split the line by any whitespace
            val words = it.split(Regex("\\s+"))
            // Count the words
            stats.wordCount = words.size
            // Iterate over the words and count the capital letters, summing the counts
            stats.capitalCount = words.stream()
                    .map(String::toCharArray)
                    .map({chars: CharArray -> chars.filter(Character::isUpperCase).count()})
                    .collect(Collectors.summingLong(Int::toLong))
            // Iterate over the words and count the vowels, summing the counts
            stats.vowelCount = words.stream()
                    .map({it.replace(Regex("(?i)[^aeiou]+"), "")})
                    .map(String::length)
                    .collect(Collectors.summingLong(Int::toLong))
            stats
        }
    }

    /**
     * ItemWriter to persist our results.  Iterates over [1-chunkSize] objects passed
     * in from the processor, aggregates the results, and persists.
     *
     * @param md5hex: String - this is a late-binding param in Spring Batch, meaning that
     * given the scope context defined on the bean it can access job-level or step-level
     * details using @Value.  In this case, we are using @JobScope to provide access to
     * the job parameters in the bean definition.  @JobScoped beans are created per job execution.
     * @StepScoped beans are important when partitioning and accessing step level metadata.
     */
    @Bean
    @JobScope
    fun resultWriter(@Value("#{jobExecutionContext[md5hex]}") md5hex: String): ItemWriter<WordProcessingStats> {
        return ItemWriter {
            val stats = statsRepo.findOne(md5hex)
            it.forEach({
                stats.capitalCount += it.capitalCount
                stats.vowelCount += it.vowelCount
                stats.wordCount += it.wordCount
            })
            statsRepo.save(stats)
        }
    }

}