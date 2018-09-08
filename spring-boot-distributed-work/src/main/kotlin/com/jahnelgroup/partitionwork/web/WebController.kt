package com.jahnelgroup.partitionwork.web

import com.hazelcast.core.HazelcastInstance
import com.hazelcast.ringbuffer.Ringbuffer
import com.jahnelgroup.partitionwork.work.Work
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebController(val hazelcastInstance: HazelcastInstance){

    val ringBuffer: Ringbuffer<Work> = hazelcastInstance.getRingbuffer<Work>("workRing")

    @PostMapping("/")
    fun accept(@RequestBody work: Work) = ringBuffer.add(work)

}