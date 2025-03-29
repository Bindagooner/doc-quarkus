package com.example.repo;

import com.example.model.Document;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;


@ApplicationScoped
public class DocumentRepository implements PanacheRepositoryBase<Document, UUID> {

}
