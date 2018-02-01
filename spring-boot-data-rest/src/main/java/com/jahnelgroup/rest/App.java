package com.jahnelgroup.rest;

import com.jahnelgroup.rest.auction.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@SpringBootApplication
public class App extends RepositoryRestConfigurerAdapter {

	private Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	/**
	 * This is a Spring core event listener, it has nothing to do with Spring Data REST.
	 * You can use this to catch Spring Data REST event's as well.
	 */
	@EventListener
	public void handle(ApplicationEvent ae){
		log.info("ApplicationEvent[thread={}] {}", Thread.currentThread().getName(), ae);
	}

	/**
	 * Steam all end-points from /api
	 *
	 * @param config
	 */
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

		// Primary IDs are not exposed by default, if you really want them
		// you must configure it here.
		config.exposeIdsFor(Person.class);

		config.setBasePath("api");
	}
}
