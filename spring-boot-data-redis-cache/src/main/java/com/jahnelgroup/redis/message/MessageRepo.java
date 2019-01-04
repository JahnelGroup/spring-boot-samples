package com.jahnelgroup.redis.message;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository <Message, Long> {

}
