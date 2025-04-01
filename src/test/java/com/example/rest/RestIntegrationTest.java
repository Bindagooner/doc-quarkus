package com.example.rest;

import com.example.model.Document;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestSecurity
public class RestIntegrationTest {

    private static final String API_PATH = "/documents";
    private static String documentId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
    }
    KeycloakTestClient keycloakClient = new KeycloakTestClient();


    @Test
    @Order(1)
    public void testCreateDocument() {
        var document1 = Document.builder()
                .id(UUID.fromString("0bdf14b8-bf6f-4967-af0a-9dc9413cf585"))
                .title("Test Document 1")
                .content("This is a test document 1")
                .tenantId("tenant-1")
                .build();

        documentId = given()
                .contentType(ContentType.JSON)
                .body(document1)
                .auth().oauth2(keycloakClient.getAccessToken("admin"))
                .when()
                .post(API_PATH)
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("title", equalTo("Test Document 1"))
                .body("content", equalTo("This is a test document 1"))
                .body("tenantId", equalTo("tenant-1"))
                .extract()
                .jsonPath()
                .getString("id");
    }

    @Test
    @Order(2)
    public void testGetDocument() {
        given()
                .pathParam("id", documentId)
                .when()
                .get(API_PATH + "/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(documentId))
                .body("title", equalTo("Test Document"))
                .body("content", equalTo("Test Content"));
    }
}
