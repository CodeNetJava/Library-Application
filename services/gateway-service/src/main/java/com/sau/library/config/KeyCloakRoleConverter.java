package com.sau.library.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//spring boot expect role in specific format "ROLE_ADMIN", but key clock sends in the nested format
// realm_access.roles format
public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String,Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
        if(resourceAccess == null || resourceAccess.isEmpty()){
            return List.of();
        }
        Map<String,Object> gatewayservice = (Map<String, Object>) resourceAccess.get("gateway-service");
       Collection<String> roles = (Collection<String>) gatewayservice.get("roles");

        return roles.stream()
                .map( e -> "ROLE_"+e)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }
}
