package com.jahnelgroup.rest.auction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "/people", collectionResourceRel = "/people")
public interface PersonRepo extends CrudRepository<Person, Long> {

}
