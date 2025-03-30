package com.example.grpc;

import com.example.auth.Roles;
import com.example.auth.SecurityContext;
import com.example.service.DocumentService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


import java.util.UUID;

@Slf4j
@GrpcService
public class DocumentProcessorService implements DocumentProcessor {

    @Inject
    DocumentService documentService;

    @Inject
    SecurityContext securityContext;

    @RolesAllowed({Roles.ADMIN, Roles.VIEWER})
    @Override
    public Uni<DocumentResponse> process(DocumentRequest request) {
        UUID id = UUID.fromString(request.getDocumentId());

        return Uni.createFrom()
                .item(() -> documentService.getDocumentById(id))
                .onItem().transform(optionalDoc -> {
                    if (optionalDoc.isEmpty()) {
                        return DocumentResponse.newBuilder()
                                .setDocumentId(request.getDocumentId())
                                .setStatus("NOT_FOUND")
                                .build();
                    }

                    com.example.model.Document doc = optionalDoc.get();
                    if (!securityContext.hasAccessToTenant(doc.getTenantId())) {
                        throw new SecurityException("Access denied: Invalid tenant");
                    }

                    return DocumentResponse.newBuilder()
                            .setDocumentId(doc.getId().toString())
                            .setStatus("PROCESSED")
                            .build();
                })
                .onFailure().recoverWithItem(throwable -> {
                    log.error("Error processing document: {}", request.getDocumentId(), throwable);
                    return DocumentResponse.newBuilder()
                            .setDocumentId(request.getDocumentId())
                            .setStatus("ERROR")
                            .build();
                });
    }
}

