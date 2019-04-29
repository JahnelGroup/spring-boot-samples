package com.jahnelgroup.oauth2.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Sets up the resource server configuration
 */
@Configuration
@Order(3)
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    DefaultTokenServices jwtTokenServices;

    @Value("${api-name: \"\"}")
    String apiName = "";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(apiName).tokenServices(jwtTokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuthRequestedMatcher())
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .anyRequest().authenticated();
    }
}

/**
 * Request matcher to filter out OAuth2 requests
 */
class OAuthRequestedMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        // Determine if the client request contained an OAuth Authorization
        Boolean haveOauth2Token = auth != null && (auth.startsWith("bearer") || auth.startsWith("Bearer"));
        Boolean haveAccessToken = request.getParameter("access_token") != null;
        return haveOauth2Token || haveAccessToken;
    }
}
