package com.example.auth;

import io.grpc.*;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTTokenParser;
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class JwtServerInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {return null;}

//        try {
//            // Check Authorization header
//            String authHeader = headers.get(AUTHORIZATION_HEADER);
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                throw new SecurityException("Missing or invalid Authorization header");
//            }
//
//
//
//
//            // Get method name being called
//            String methodName = call.getMethodDescriptor().getBareMethodName();
//
//            // Check roles based on method
//            checkMethodAccess(methodName);
//
//            return next.startCall(call, headers);
//
//        } catch (SecurityException e) {
//            call.close(Status.PERMISSION_DENIED.withDescription(e.getMessage()), new Metadata());
//            return new ServerCall.Listener<ReqT>() {};
//        } catch (Exception e) {
//            call.close(Status.UNAUTHENTICATED.withDescription("Authentication failed"), new Metadata());
//            return new ServerCall.Listener<ReqT>() {};
//        }
//    }
//
//    private void checkMethodAccess(String methodName) {
//        // Get roles from realm_access claim
//        JsonObject realmAccess = jwt.getClaim("realm_access");
//        if (realmAccess == null) {
//            throw new SecurityException("No roles found in token");
//        }
//
//        JsonArray roles = realmAccess.getJsonArray("roles");
//        if (roles == null) {
//            throw new SecurityException("No roles found in token");
//        }
//
//        Set<String> userRoles = roles.stream()
//                .map(JsonString.class::cast)
//                .map(JsonString::getString)
//                .collect(Collectors.toSet());
//
//        // Check method-specific role requirements
//        switch (methodName) {
//            case "Process":
//                if (!hasAnyRole(userRoles, "admin", "viewer")) {
//                    throw new SecurityException("Requires admin or viewer role");
//                }
//                break;
//            default:
//                throw new SecurityException("Unknown method: " + methodName);
//        }
//
//        // Check tenant_id if needed
//        String tenantId = jwt.getClaim("tenant_id");
//        if (tenantId == null) {
//            throw new SecurityException("No tenant_id found in token");
//        }
//    }

//    private boolean hasRole(Set<String> userRoles, String role) {
//        return userRoles.contains(role);
//    }
//
//    private boolean hasAnyRole(Set<String> userRoles, String... roles) {
//        return Arrays.stream(roles).anyMatch(userRoles::contains);
//    }
}

