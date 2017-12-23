# Spring Boot Jackson Field Security

A sample application demonstrating how to use [JahnelGroup/jackson-field-security](https://github.com/JahnelGroup/jackson-field-security) to protect access to your your JSON model on fields-by-field basis with Jackson.

## Getting Started

[Jackson](https://github.com/FasterXML/jackson) is the standard library for JSON parsing and modeling in Java. The core library doesn't have the ability to conditional control access to individual fields of the JSON model. Today you have to create entire protected views for your that can be overwhelming to absorb in to your design. We created a library to add this missing to Jackson and this sample illustrates how simple it is to use. 

## How it works

Here we are leveraging [Spring Data REST](https://projects.spring.io/spring-data-rest/) to very quickly provide simple http CRUD operations. 

The two primary data models used in this sample are **Person** and **Club**. To get into a Club the Person would need the Club's secret phrase. Only users who are part of the Club will see that field, others will not.

