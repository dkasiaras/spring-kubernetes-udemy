package com.kasiarakos.gatewayserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        var realmAccess = jwt.getClaims().get("realm_access");
        if (realmAccess == null) {
            return Collections.emptyList();
        }

        if (realmAccess instanceof Map<?, ?> realmAccessMap) {
            var roles = realmAccessMap.get("roles");
            if (roles instanceof List<?> rolesList) {
                return rolesList.stream().map(roleName -> MessageFormat.format("ROLE_{0}", roleName))
                        .map(SimpleGrantedAuthority::new)
                        .map(GrantedAuthority.class::cast)
                        .toList();
            }
        }
        return Collections.emptyList();
    }
}
