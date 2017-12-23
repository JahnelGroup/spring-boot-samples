package com.jahnelgroup.datarestsecurity.supplier

import org.slf4j.LoggerFactory
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.HandleBeforeCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component

@Component
@RepositoryEventHandler
class SupplierHandler {

    private val logger = LoggerFactory.getLogger(SupplierHandler::class.java)

    @HandleBeforeCreate
    fun beforeSupplierCreate(supplier : Supplier){
        logger.info("beforeSupplierCreate: {}", supplier)
    }

    @HandleAfterCreate
    fun afterSupplerCreate(supplier : Supplier){
        logger.info("afterSupplerCreate: {}", supplier)
    }

}