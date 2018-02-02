package com.jahnelgroup.jpa.one_to_one

import org.springframework.data.repository.CrudRepository

interface SsnRepo : CrudRepository<Ssn, Long>