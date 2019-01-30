package com.jahnelgroup.resttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@Configuration
@RestController
@SpringBootApplication
public class App {

	private Logger logger = LoggerFactory.getLogger(App.class);

	@Value("${instance-id}")
	String instanceId;

	@Value("${hello-host}")
	String helloHost;

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Autowired
	RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@GetMapping("/")
	public String hello(){
		return "Hello from instance " + instanceId;
	}

	@Scheduled(initialDelay = 5000, fixedRate = 5000)
	public void scheduledHello(){
		logger.info("I am instance {} and received '{}'",
				instanceId,
				restTemplate.getForObject("http://"+helloHost, String.class));
	}

}