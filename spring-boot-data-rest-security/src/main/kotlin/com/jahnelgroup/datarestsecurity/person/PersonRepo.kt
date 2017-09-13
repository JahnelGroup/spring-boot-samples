package com.jahnelgroup.datarestsecurity.person

import org.springframework.data.repository.CrudRepository
import org.springframework.security.access.prepost.PreAuthorize

@PreAuthorize("hasRole('ROLE_USER')")
interface PersonRepo : CrudRepository<Person, Long>{

    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun delete(entities: MutableIterable<Person>?)
    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun delete(entity: Person?)
    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun delete(id: Long?)
    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun deleteAll()

}