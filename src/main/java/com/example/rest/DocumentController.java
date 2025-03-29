package com.example.rest;

import com.example.model.Document;
import com.example.service.DocumentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Objects;
import java.util.UUID;

@Path("/api/documents")
@Produces(MediaType.APPLICATION_JSON)
public class DocumentController {

    @Inject
    private DocumentService documentService;


    @GET
    @Path("/{id}")
    public Response getDocumentById(@PathParam("id") UUID id) {
        var document = documentService.getDocumentById(id);
        return Objects.isNull(document) ?
                Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(document).build();
    }

}

