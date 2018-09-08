package com.jahnelgroup.queue.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

@EnableScheduling
@SpringBootApplication
public class App {

	public final static UUID uuid = UUID.randomUUID();

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}