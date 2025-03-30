package com.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RestIntegrationTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testGetDocument() {
        given()
            .when()
                .get("/api/documents/{id}", 1)
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("title", notNullValue())
                .body("content", notNullValue());
    }

    @Test
    public void testCreateDocument() {
        String document = """
            {
                "title": "Test Document",
                "content": "Test Content"
            }""";

        given()
            .contentType(ContentType.JSON)
            .body(document)
        .when()
            .post("/api/documents")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("id", notNullValue())
            .body("title", equalTo("Test Document"))
            .body("content", equalTo("Test Content"));
    }
}

