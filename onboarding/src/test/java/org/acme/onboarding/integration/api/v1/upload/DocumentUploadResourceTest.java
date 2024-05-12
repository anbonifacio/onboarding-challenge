package org.acme.onboarding.integration.api.v1.upload;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.client.OidcTestClient;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.repository.OcrRepository;
import org.acme.onboarding.integration.DbIntegrationUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static java.util.concurrent.CompletableFuture.completedStage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@QuarkusTest
class DocumentUploadResourceTest {
    static final OidcTestClient authClient = new OidcTestClient();

    @InjectMock
    OcrRepository ocrRepository;

    @Inject
    EntityManager em;

    @AfterEach
    @Transactional
    void tearDown() {
        var dropDb = DbIntegrationUtils.readFile("drop_db.sql");
        DbIntegrationUtils.runSqlScript(dropDb, em);
    }

    @Test
    void shouldGive204WhenDocumentIsUploaded() {
        // given
        var token = authClient.getAccessToken("alice", "alice");
        var ocrResponse = Instancio.of(ProcessedDocument.class).create();
        given(ocrRepository.processDocument(any(File.class))).willReturn(completedStage(ocrResponse));

        var multipart = new MultiPartSpecBuilder("Test-Content-In-File".getBytes())
                .fileName("image.jpg")
                .controlName("image")
                .mimeType("image/jpeg")
                .build();

        // when
        var result = RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(token)
                .multiPart(multipart)
                .when()
                .put("/api/v1/documents")
                .then();

        // then
        result.statusCode(204);
    }

    @Test
    void shouldGive403WhenEmailValidationIsMissing() {
        // given
        var token = authClient.getAccessToken("jdoe", "jdoe");

        var multipart = new MultiPartSpecBuilder("Test-Content-In-File".getBytes())
                .fileName("image.jpg")
                .controlName("image")
                .mimeType("image/jpeg")
                .build();

        // when
        var result = RestAssured.given()
                .auth()
                .oauth2(token)
                .multiPart(multipart)
                .when()
                .put("/api/v1/documents")
                .then();

        // then
        result.statusCode(403);
    }

    @Test
    void shouldGive401WhenTokenIsMissing() {
        // given
        var multipart = new MultiPartSpecBuilder("Test-Content-In-File".getBytes())
                .fileName("image.jpg")
                .controlName("image")
                .mimeType("image/jpeg")
                .build();

        // when
        var result = RestAssured.given()
                .multiPart(multipart)
                .when()
                .put("/api/v1/documents")
                .then();

        // then
        result.statusCode(401);
    }

    @Test
    void shouldGive400WhenMultipartIsMissing() {
        // given
        var token = authClient.getAccessToken("alice", "alice");

        // when
        var result = RestAssured.given()
                .auth()
                .oauth2(token)
                .when()
                .put("/api/v1/documents")
                .then();

        // then
        result.statusCode(400);
    }
}
