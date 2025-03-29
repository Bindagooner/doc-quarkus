package com.example.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Document {
    private UUID id;
    private String name;
    private String url;
}
