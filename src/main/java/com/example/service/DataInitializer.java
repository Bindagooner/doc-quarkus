package com.example.service;

import com.example.model.Document;
import com.example.repo.DocumentRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.UUID;

@ApplicationScoped
@Slf4j
public class DataInitializer {

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Inject
    Vertx vertx;

    void onStart(@Observes StartupEvent ev) {
        vertx.runOnContext(v -> {
            log.info("Initializing test data");
            initData()
                    .subscribe().with(
                            success -> log.info("Test data initialized successfully"),
                            error -> log.error("Error initializing test data", error)
                    );
        });
    }

    private Uni<Void> initData() {

        return documentRepository.count()
                .chain(count -> {
                    if (count == 0) {
                        log.info("No documents found, initializing test documents");
                        return createTestDocuments();
                    }
                    log.info("Found {} existing documents, skipping initialization", count);
                    return Uni.createFrom().voidItem();
                });
    }

    private Uni<Void> createTestDocuments() {
        var document1 = Document.builder()
                .id(UUID.fromString("0bdf14b8-bf6f-4967-af0a-9dc9413cf585"))
                .title("Test Document 1")
                .content("This is a test document 1")
                .tenantId("tenant-1")
                .build();

        var document2 = Document.builder()
                .id(UUID.fromString("a914e95f-483a-45dc-bee4-dab2ce4ecd1f"))
                .title("Test Document 2")
                .content("This is a test document 2")
                .tenantId("tenant-1")
                .build();

        return Panache.withTransaction(() ->
                Uni.combine().all().unis(
                                documentRepository.persist(document1),
                                documentRepository.persist(document2)
                        )
                        .discardItems()
        );
    }
}

