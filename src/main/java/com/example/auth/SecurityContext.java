package com.example.auth;


import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SecurityContext {

    @Inject
    private CurrentIdentityAssociation currentIdentityAssociation;


    public boolean hasAccessToTenant(String tenantId) {
        var tenantId1 = currentIdentityAssociation.getIdentity().getAttribute("tenantId");
        return tenantId.equals(tenantId1);
    }

    public Uni<Boolean> hasAccessToTenantAsync(String tenantId) {
        return currentIdentityAssociation.getDeferredIdentity()
                .onItem().transform(identity -> {
                    var tenantId1 = identity.getAttribute("tenantId");
                    return tenantId.equals(tenantId1);
                });
    }

}
