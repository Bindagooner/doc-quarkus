package com.example.auth;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {

    @Override
    public Response toResponse(SecurityException e) {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(new ErrorResponse("ACCESS_DENIED", e.getMessage()))
                .build();
    }
}
