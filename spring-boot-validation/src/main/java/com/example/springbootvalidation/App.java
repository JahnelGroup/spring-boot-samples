package com.example.springbootvalidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.util.Locale;


@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Autowired
	private MessageSource messageSource;

	@PostConstruct
	void init(){
		String notNull = messageSource.getMessage("NotNull", null, Locale.getDefault());
		System.out.println("NotNull from messages.properties = " + notNull);
	}

}
