Spring Boot Thymeleaf
===================================
Spring Boot version of [php-samples/todo-list](https://github.com/JahnelGroup/php-samples/tree/master/todo-list). 

### Run

#### Docker-Compose
Bring up the entire stack with:

```bash
$ gradle composeUp
```

#### Adminer
Validate that the database is up and schema was created with Adminer (previously known as phpMyAdmin). Navigate to [http://localhost:8080](http://localhost:8080) and login:

* System: MySQL
* Server: db
* Username: root
* Password: rootpassword
* Database: todolist

#### App
The applicationc can be located at [http://localhost](http://localhost).

### Structure 
The basic structure of the application is as follows.

```
/spring-boot-thymeleaf/
├── build.gradle
├── Dockerfile
├── docker-compose.yml
├── src/main
│   └── com/jahnelgroup/todo
│       └── App.kt
│   └── resources
│       └── templates
│           └── *.html
│       └── application.properties
```

### MySQL
The root password is defined in [.env](./.env) and loaded as an environment variable in the [docker-compose.yml](./docker-compose.yml) file.

```
envrionment:
      MYSQL_ROOT_PASSWORD: "${DB_ROOT_PASSWORD}"
```

The default schema is generated by Hibernate based on the domain modeling.

### Spring
The entire backend is located in [App.kt](./src/main/kotlin/com/jahnelgroup/todo/App.kt).