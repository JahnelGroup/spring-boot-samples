@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package com.jahnelgroup.acl.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect

/**
 * Expression-Based Access Control
 *      https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#el-access
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(var dataSource: DataSource) : WebSecurityConfigurerAdapter() {

    @Value("\${security.password-encoder.security-strength}")
    lateinit var securityStrength: Integer

    // Enabled sec: tag for Thymeleaf
    // https://github.com/thymeleaf/thymeleaf-extras-springsecurity/issues/61
    // https://github.com/thymeleaf/thymeleaf-extras-springsecurity#configuration
    @Bean
    fun securityDialect(): SpringSecurityDialect {
        return SpringSecurityDialect()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(securityStrength.toInt())
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        // https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#appendix-schema
        // To debug set breakpoints in JdbcDaoImpl.java
        var jdbcAuth = auth.jdbcAuthentication()
        jdbcAuth.dataSource(dataSource).passwordEncoder(passwordEncoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/login*").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().fullyAuthenticated()
        .and()
            .headers().frameOptions().disable() // h2
        .and()
            .csrf().disable()
        .formLogin()
            .loginPage("/login.html")
        .and().exceptionHandling().accessDeniedPage("/accessDenied.html")
    }
}