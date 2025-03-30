package com.example.auth;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.auth.Roles.ADMIN;

@ApplicationScoped
public class SecurityContext {
    @Inject
    JsonWebToken jwt;

    public String getCurrentTenantId() {
        return jwt.getClaim("tenant_id");
    }

    public boolean hasAccessToTenant(String tenantId) {
        return getCurrentTenantId().equals(tenantId);
    }

}
