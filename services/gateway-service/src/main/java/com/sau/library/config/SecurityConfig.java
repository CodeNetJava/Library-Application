package com.sau.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

//We will define all our security rules in a SecurityWebFilterChain bean.
//This tells the gateway which endpoints are public and which ones require specific roles
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){

        http.authorizeExchange(exchange ->
                exchange
                        // make login and register public
                        // note: path prefix "/login-service"
                        .pathMatchers("/login-service/library/login","/login-service/library/register/consumer")
                        .permitAll()
                        .pathMatchers("/login-service/library/register/admin").authenticated()
                        // need admin role only
                        .pathMatchers("/login-service/library/admin/**").hasRole("ADMIN")
                        // anyrequest to book /order / user service need role consumer or admin
                        .pathMatchers("/user-service/library/user/**","/book-service/library/book/**","/order-service/library/order/**")
                        .hasAnyRole("CONSUMER","ADMIN")
                        .anyExchange().authenticated()
                         )
                .oauth2ResourceServer(oauth2 ->  oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtracter())
                ))
                .csrf(ServerHttpSecurity.CsrfSpec::disable); //disable csrf for stateless apis
    return http.build();
    }

    // this converter extract roles from the jwt
    //maps it to the spring security's grantedAuthority
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtracter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
