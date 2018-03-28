//package com.example.springbootvalidation;
//
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.validation.Validator;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
//@Configuration
//public class AppConfig {
//
//    /**
//     * Makes the codes in messages.properties available to Hibernate validator.
//     */
//    @Bean
//    public Validator validator(MessageSource messageSource) {
//        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//        bean.setValidationMessageSource(messageSource);
//        return bean;
//    }
//
//}
