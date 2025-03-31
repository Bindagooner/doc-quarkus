package com.example.rest;

import com.example.auth.ErrorResponse;
import com.example.auth.Roles;
import com.example.auth.SecurityContext;
import com.example.auth.TenantSecured;
import com.example.model.Document;
import com.example.repo.DocumentRepository;
import com.example.service.DocumentService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/documents")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class DocumentResource {

    @Inject
    private DocumentService documentService;

    @Inject
    private DocumentRepository repository;

    @Inject
    private SecurityContext securityContext;

    @GET
    @Path("/{id}")
    @RolesAllowed({Roles.ADMIN, Roles.VIEWER})
    @TenantSecured
    public Uni<Document> getDocumentById(@PathParam("id") UUID id) {

        return documentService.getDocumentById(id)
                .onItem()
                .ifNotNull().transform(doc -> {
                    if (!securityContext.hasAccessToTenant(doc.getTenantId())) {
                        throw new SecurityException("Access denied: Invalid tenant");
                    }
                    return doc;
                })
                .onItem().ifNull().failWith(() -> new NotFoundException("Document not found"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.ADMIN})
    public Uni<Response> createDocument(Document document) {
        return documentService.saveDocument(document)
                .onItem().transform(savedDoc ->
                        Response.status(Response.Status.CREATED)
                                .entity(savedDoc)
                                .build()
                )
                .onFailure().recoverWithItem(throwable -> {
                    log.error("Error saving document", throwable);
                    if (throwable instanceof SecurityException) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity(new ErrorResponse("403", "Access denied: " + throwable.getMessage()))
                                .build();
                    }
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ErrorResponse("500", "Error saving document"))
                            .build();
                });
    }


    @GET
    public Uni<Long> count(){
        return repository.count();
    }

}

