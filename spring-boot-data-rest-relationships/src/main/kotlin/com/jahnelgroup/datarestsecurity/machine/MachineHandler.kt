package com.jahnelgroup.datarestsecurity.machine

import org.slf4j.LoggerFactory
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler
class MachineHandler {

    private val logger = LoggerFactory.getLogger(MachineHandler::class.java)

    @HandleBeforeCreate
    fun beforeMachineCreate(machine : Machine){
        logger.info("beforeMachineCreate: {}", machine)
    }

    @HandleAfterCreate
    fun afterMachineCreate(machine : Machine){
        logger.info("afterMachineCreate: {}", machine)
    }

}