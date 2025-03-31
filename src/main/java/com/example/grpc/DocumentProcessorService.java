package com.example.grpc;

import com.example.auth.JwtServerInterceptor;
import com.example.auth.Roles;
import com.example.auth.SecurityContext;
import com.example.repo.DocumentRepository;
import com.example.service.DocumentService;
import io.quarkus.grpc.GrpcService;
import io.quarkus.grpc.RegisterInterceptor;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


import java.util.UUID;

@Slf4j
@GrpcService
//@RegisterInterceptor(JwtServerInterceptor.class)
public class DocumentProcessorService implements DocumentProcessor {

    @Inject
    DocumentRepository documentRepository;

    @Inject
    SecurityContext securityContext;

    @RolesAllowed({Roles.ADMIN})
    @Override
    @WithTransaction
    public Uni<DocumentResponse> process(DocumentRequest request) {
        UUID id = UUID.fromString(request.getDocumentId());


        return documentRepository.findById(id)
                .onItem().transform(doc -> {
                    if (doc == null) {
                        return DocumentResponse.newBuilder()
                                .setDocumentId(request.getDocumentId())
                                .setStatus("NOT_FOUND")
                                .build();
                    }

//                    if (!securityContext.hasAccessToTenant(doc.getTenantId())) {
//                        throw new SecurityException("Access denied: Invalid tenant");
//                    }

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

