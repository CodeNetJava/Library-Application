package com.sau.library.filter;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Configuration
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // ServerWebExchange holds the current request and response
        // GatewayFilterChain lets request pass to the next filter
        return exchange.getPrincipal()  // this extract the Authenticated user(principle) wrapped in the mono
                .cast(JwtAuthenticationToken.class) // cast principle to the JwtToken
                .flatMap(authetication -> {
                    Jwt jwt = authetication.getToken();
                    String userId = jwt.getSubject();
                    String username = jwt.getClaimAsString("preferred_username");
                    String firstname = jwt.getClaimAsString("given_name");
                    String lastname = jwt.getClaimAsString("family_name");
                    String email = jwt.getClaimAsString("email");
                    String roles = getRoles(authetication);

                    // This adds custom headers to the incoming request using a mutable builder.
                    // Useful for downstream services to get user info without decoding the token again.
                    ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", userId)
                            .header("X-Username", username)
                            .header("X-User-Roles", roles)
                            .header("X-Email", email)
                            .header("X-First-Name", firstname)
                            .header("X-Last-Name", lastname)
                            .build();
                    // Create a new exchange with the mutated request
                    ServerWebExchange serverWebExchange = exchange.mutate().request(serverHttpRequest).build();
                    // Pass the new exchange to the next filter in the chain
                    return chain.filter(serverWebExchange);
                }).switchIfEmpty(chain.filter(exchange)); // If no principal, continue the chain without modification

    }

    private String getRoles(JwtAuthenticationToken authetication) {
        return authetication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(e -> e.replace("ROLE_", ""))
                .collect(Collectors.joining(","));
    }

    //This filter runs after Spring Security authentication
    @Override
    public int getOrder() {
        return -1;
    }
}
