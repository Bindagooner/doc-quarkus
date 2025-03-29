package com.example.service;

import com.example.model.Document;
import com.example.repo.DocumentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@ApplicationScoped
@Slf4j
public class DocumentService {

    @Inject
    DocumentRepository documentRepository;

    public DocumentService() {
        log.info("DocumentService created");
    }

    @PostConstruct
    void init() {
        if (documentRepository.count() == 0) {
            var uuid1 = UUID.fromString("0bdf14b8-bf6f-4967-af0a-9dc9413cf585");

            var uuid2 = UUID.fromString("a914e95f-483a-45dc-bee4-dab2ce4ecd1f");

            var document1 = Document.builder()
                    .id(uuid1)
                    .title("Test Document 1")
                    .content("This is a test document 1")
                    .tenantId("tenant-1")
                    .build();
            var document2 = Document.builder()
                    .id(uuid2)
                    .title("Test Document 2")
                    .content("This is a test document 2")
                    .tenantId("tenant-2")
                    .build();
            saveDocument(document1);
            saveDocument(document2);
            log.info("init saved test doc");
        }
    }

    @Transactional
    public void saveDocument(Document document) {
        log.info("Saving document: {}", document);
        documentRepository.persist(document);
        log.info("saved docuemnt. ID: {}", document.getId());
    }

    public Document getDocumentById(UUID id) {
        log.info("Getting document by id: {}", id);
        return documentRepository.findById(id);
    }
}
