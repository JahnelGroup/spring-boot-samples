package com.jahnelgroup.partitionwork.work

import com.hazelcast.core.HazelcastInstance
import com.hazelcast.ringbuffer.Ringbuffer
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.Executors
import javax.annotation.PostConstruct

@Component
class Worker(val hazelcastInstance: HazelcastInstance){

    private val ringBuffer: Ringbuffer<Work> = hazelcastInstance.getRingbuffer<Work>("workRing")
    private val uuid = UUID.randomUUID().toString()

    @PostConstruct
    fun begin() = Executors.newSingleThreadExecutor().submit {
        var sequence = ringBuffer.headSequence()
        while(true){
            val work = ringBuffer.readOne(sequence)
            println("[$uuid:$sequence] $work")

            sequence++
        }
    }



}