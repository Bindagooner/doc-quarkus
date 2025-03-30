package com.example.rest;

import com.example.auth.Roles;
import com.example.auth.SecurityContext;
import com.example.auth.TenantSecured;
import com.example.model.Document;
import com.example.service.DocumentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import java.util.UUID;

@Path("/documents")
@Produces(MediaType.APPLICATION_JSON)
public class DocumentResource {

    @Inject
    private DocumentService documentService;

    @Inject
    private SecurityContext securityContext;

    @GET
    @Path("/{id}")
    @RolesAllowed({Roles.ADMIN, Roles.VIEWER})
    @TenantSecured
    public Response getDocumentById(@PathParam("id") UUID id) {
        Optional<Document> documentOpt = documentService.getDocumentById(id);

        if (documentOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            var doc = documentOpt.get();
            if (!securityContext.hasAccessToTenant(doc.getTenantId())) {
                throw new SecurityException("Access denied: Invalid tenant");
            }
            return Response.ok(doc).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.ADMIN})
    public Response createDocument(Document document) {
        documentService.saveDocument(document);
        return Response.status(Response.Status.CREATED)
                .entity(document)
                .build();
    }

}

