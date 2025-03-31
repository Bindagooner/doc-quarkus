package com.example.auth;

import io.grpc.Metadata;
import io.quarkus.grpc.auth.GrpcSecurityMechanism;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.smallrye.jwt.runtime.auth.JsonWebTokenCredential;
import jakarta.inject.Singleton;


@Singleton
public class GrpcCustomSecurityMechanism implements GrpcSecurityMechanism {

    private static final Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean handles(Metadata metadata) {
        String authHeader = metadata.get(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header");
        }
        return true;
    }

    @Override
    public AuthenticationRequest createAuthenticationRequest(Metadata metadata) {
        String token = metadata.get(AUTHORIZATION_HEADER).substring(BEARER_PREFIX.length());

        TokenAuthenticationRequest request = new TokenAuthenticationRequest(
               new JsonWebTokenCredential(token));
        return request;
    }

}
