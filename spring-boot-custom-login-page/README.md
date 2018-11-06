# Spring Boot Custom Login Page

This is a basic Sring Boot application that demonstrates how to override the default login page provided by spring-boot-starter-security.

## Run the App

Start the application with gradle and then navigate to [http://localhost:8080](http://localhost:8080).

```bash
$ gradle bootRun
...
2018-10-30 22:43:41.938  INFO 6917 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
```

## Username and Password

The default username and password have been declared in **src/main/resources/application.properties**.

```
spring.security.user.name=user
spring.security.user.password=pass
```

## Java File

There is one Java **src/main/java/com/example/demo/App.java** which is doing a few different things.

* It starts the Spring ApplicationContext with @SpringBootApplication
* It defines an inner class called Security that is doing two things.
  * Extends WebSecurityConfigurerAdapter and overrides the configure method to define Security rules.
  * Implements WebMvcConfigurer and overrides addViewControllers to configure a mapping for the login url.

```java
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Configuration
	class Security extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

                // from WebSecurityConfigurerAdapter
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				// permit images and css only (js would be appropriate too if we had any)
				.authorizeRequests()
					.antMatchers("/images/**", "/css/**").permitAll()
					.anyRequest().authenticated()
				// indicate that the login form is found at /login
				.and()
					.formLogin().loginPage("/login").permitAll()
				// default login url is /logout
				.and()
					.logout().permitAll();
		}

                // from WebMvcConfigurer
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			//maps urlsto a view which will serve up the listed file
			registry.addViewController("/login").setViewName("login");
			registry.addViewController("/logout").setViewName("index");
		}
	}

}
```

## Resources

Spring will automatically configure **src/main/resources** to be on the classpath for serving web content. Static is where your _static_ content goes (i.e., images, css, javascript) and templates is where your _dynamic_ content goes (i.e., thymeleaf, jsp).

Important files:

* **templates/login.html** - The login page
* **static/index.html** - The protected content (it's the default page loaded by the webserver).
