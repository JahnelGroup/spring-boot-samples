package com.jahnelgroup.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@EnableCaching
@SpringBootApplication
public class App implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void run(String... args) throws Exception {
		log.info("Redis Clients:");
		redisTemplate.getClientList().forEach(msg -> log.info("\t"+msg.toString()));
	}
}
