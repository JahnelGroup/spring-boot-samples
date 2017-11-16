package com.jahnelgroup.datarestsecurity.machine

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

interface MachineRepo : CrudRepository<Machine, Long>