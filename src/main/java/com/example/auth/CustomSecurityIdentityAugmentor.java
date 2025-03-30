package com.example.auth;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomSecurityIdentityAugmentor implements SecurityIdentityAugmentor {

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        // Only process JWT tokens
        if (!(identity.getPrincipal() instanceof JsonWebToken)) {
            return Uni.createFrom().item(identity);
        }

        JsonWebToken jwt = (JsonWebToken) identity.getPrincipal();

        // Extract roles from realm_access claim
        Set<String> roles = Optional.ofNullable(jwt.<JsonObject>getClaim("realm_access"))
                .map(realm -> realm.getJsonArray("roles"))
                .map(rolesArray -> rolesArray.stream()
                        .map(JsonString.class::cast)
                        .map(JsonString::getString)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        // Build new identity with custom roles
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
        roles.forEach(builder::addRole);

        return Uni.createFrom().item(builder.build());
    }

    @Override
    public int priority() {
        // Higher priority runs first
        return 100;
    }
}
