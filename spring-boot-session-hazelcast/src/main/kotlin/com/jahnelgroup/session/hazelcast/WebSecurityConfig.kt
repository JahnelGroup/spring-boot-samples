package com.jahnelgroup.session.hazelcast

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebSecurityConfig() : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
                .antMatchers("/manage/**").permitAll()
                .antMatchers("/api/unprotected/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .defaultSuccessUrl("/welcome.html", true)
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
            .sessionManagement()
                .and()
            .csrf()
                .disable()
    }

    @Throws(Exception::class)
    override fun configure(auth : AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER")
    }
}
