package com.jahnelgroup.oauth2.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * Sets up the authorization configuration
 */
@Configuration
@EnableAuthorizationServer
public class InMemoryClientAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${api-name: \"\"}")
    String apiName = "";

    @Value("${client.name: \"\"}")
    String clientName = "";

    @Value("${client.secret: \"\"}")
    String clientSecret = "";

    @Value("${token-validity-length: 0}")
    Integer tokenValidityLength = 0;

    @Value("${jks.file-name: \"\"}")
    String jksFileName = "";

    @Value("${jks.secret: \"\"}")
    String jksSecret = "";


    /**
     * Used to store and validate client details
     *
     * @return an in memory implementation of [ClientDetailsService]
     */
    @Bean
    public ClientDetailsService inMemoryClientDetailsService() {
        return new InMemoryClientDetailsService();
    }

    /**
     * Configures client details in memory
     *
     * @param clients a configurer to modify the client details
     */
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(clientName)
            .resourceIds(apiName)
            .scopes("read", "write")
            .secret(passwordEncoder.encode(clientSecret))
            .authorizedGrantTypes("client_credentials", "authorization", "refresh_token")
            .authorities("ROLE_TRUSTED_CLIENT")
            .accessTokenValiditySeconds(tokenValidityLength)
            .refreshTokenValiditySeconds(tokenValidityLength)
            .autoApprove(true);
    }

    /**
     * Configures a JwtAccessTokenConverter that will be able to create and verify access tokens
     *
     * As an app that is both an auth server and resource server it has both private/public
     * key pair. Any additional resource servers will only have the public key pair.
     *
     * @return an implemented JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(jksFileName + ".jks"),
            jksSecret.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(jksFileName));
        return converter;
    }

    /**
     * Configures authorization end points
     *
     * @param endpoints a configurer to modify the authorization server endpoints
     */
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
            .accessTokenConverter(accessTokenConverter())
            .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Configures the [DefaultTokenServices] to be used
     *
     * @return a configured [DefaultTokenServices]
     */
    @Bean
    @Primary
    public DefaultTokenServices jwtTokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore());
        services.setSupportRefreshToken(true);
        return services;
    }

}
