package org.acme.onboarding.integration.id_document.jpa;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.integration.DbIntegrationUtils;
import org.acme.onboarding.outbound.id_document.repository.jpa.entity.IdDocumentEntity;
import org.acme.onboarding.outbound.id_document.repository.jpa.impl.IdDocumentRepositoryImpl;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

@QuarkusTest
class IdDocumentRepositoryIntegrationTest {
    @Inject
    IdDocumentRepositoryImpl sut;

    @Inject
    EntityManager em;

    @Inject
    ManagedExecutor managedExecutor;

    @BeforeEach
    @Transactional
    void setUp() {
        sut = new IdDocumentRepositoryImpl(em, managedExecutor);

        var dbPreScript = DbIntegrationUtils.readFile("prepare_db.sql");
        var affectedRows = DbIntegrationUtils.runSqlScript(dbPreScript, em);
        assertThat(affectedRows).isEqualTo(2);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        var dbPostScript = DbIntegrationUtils.readFile("drop_db.sql");
        DbIntegrationUtils.runSqlScript(dbPostScript, em);
    }

    @Test
    void shouldFindExpiredDocumentEmails() {
        // when
        var result = sut.findEmailsForExpiredDocuments();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    @Transactional
    void shouldSaveDocumentData() {
        // given
        var processedDocument = Instancio.of(ProcessedDocument.class).create();
        var username = Instancio.of(Username.class).create();
        var email = Instancio.of(Email.class).create();

        // when
        var result = sut.persistDocumentData(processedDocument, username, email)
                .thenCompose(unused -> supplyAsync(this::countDb, managedExecutor));

        // then
        assertThat(result).succeedsWithin(2, TimeUnit.SECONDS).matches(count -> count.equals(3L), "count should be 3");
    }

    @Test
    @Transactional
    void shouldUpdateDocumentData() {
        // given
        var processedDocument = Instancio.of(ProcessedDocument.class).create();
        var username = new Username("B");
        var email = Instancio.of(Email.class).create();

        // when
        var result = sut.persistDocumentData(processedDocument, username, email)
                .thenCompose(unused -> supplyAsync(
                        () -> em.createQuery(
                                        "select iddoc from IdDocumentEntity iddoc where iddoc.username = 'B'",
                                        IdDocumentEntity.class)
                                .getSingleResult(),
                        managedExecutor));
        // then
        assertThat(result)
                .succeedsWithin(2, TimeUnit.SECONDS)
                .matches(entity -> entity.getUpdateDatetime() != null, "updateDatetime is null")
                .matches(
                        entity -> entity.getIdDocumentNumber()
                                .equals(processedDocument
                                        .idDocumentNumber()
                                        .value()
                                        .toUpperCase()),
                        "idDocumentNumber not equal")
                .matches(entity -> entity.getEmail().equals(email.value().toLowerCase()), "email not equal")
                .matches(
                        entity -> entity.getIdDocumentExpiringDate().equals(processedDocument.expiringDate()),
                        "expiringDate not equal");
    }

    private Long countDb() {
        return em.createQuery("select count(*) from IdDocumentEntity", Long.class)
                .getSingleResult();
    }
}
