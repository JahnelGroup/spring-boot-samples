package com.jahnelgroup.datarestsecurity.supplier

import com.jahnelgroup.datarestsecurity.machine.MachineRepo
import org.springframework.data.rest.webmvc.RepositoryLinksResource
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.data.rest.webmvc.RepositorySearchesResource
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceProcessor
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ResponseBody

@RepositoryRestController
class SupplierController  (
        val repoRestConfig: RepositoryRestMvcConfiguration,
        val supplierRepo: SupplierRepo,
        val machineRepo: MachineRepo
) : ResourceProcessor<Resource<Supplier>> {

    override fun process(resource: Resource<Supplier>): Resource<Supplier> {
        val method = ControllerLinkBuilder.methodOn(this::class.java).createSupplierWithMachines(Supplier())
        resource.add( ControllerLinkBuilder.linkTo(method).withRel("createSupplierWithMachines") )
        return resource
    }

//    override fun process(resource: RepositoryLinksResource): RepositoryLinksResource {
//        //if( Supplier::class.java.name.equals(resource..) ){
//            val method = ControllerLinkBuilder.methodOn(this::class.java).createSupplierWithMachines(Supplier())
//            resource.add( ControllerLinkBuilder.linkTo(method).withRel("createSupplierWithMachines") )
//        //}
//        return resource
//    }

    @ResponseBody
    fun createSupplierWithMachines(supplier : Supplier) : ResponseEntity<Any> {
        val savedSupplier = supplierRepo.save(supplier)
        return ResponseEntity.ok(savedSupplier)
    }

}