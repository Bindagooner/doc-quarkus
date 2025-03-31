package com.example.repo;

import com.example.model.Document;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@ApplicationScoped
@Slf4j
public class DocumentRepository implements PanacheRepositoryBase<Document, UUID> {

    public Uni<Document> findByIdAndTenantId(UUID id, String tenantId) {
        return find("id = ?1 and tenantId = ?2", id, tenantId).firstResult();
    }

}
