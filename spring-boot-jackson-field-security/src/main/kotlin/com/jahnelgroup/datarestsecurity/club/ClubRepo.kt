package com.jahnelgroup.datarestsecurity.club

import org.springframework.data.repository.CrudRepository
import org.springframework.security.access.prepost.PreAuthorize

@PreAuthorize("hasRole('ROLE_USER')")
interface ClubRepo : CrudRepository<Club, Long>{

    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun delete(entities: MutableIterable<Club>?)
    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun delete(entity: Club?)
    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun delete(id: Long?)
    @PreAuthorize("hasRole('ROLE_ADMIN')") override fun deleteAll()

}