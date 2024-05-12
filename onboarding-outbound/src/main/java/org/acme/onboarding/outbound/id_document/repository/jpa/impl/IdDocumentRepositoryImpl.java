package org.acme.onboarding.outbound.id_document.repository.jpa.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.domain.repository.IdDocumentRepository;
import org.acme.onboarding.outbound.id_document.repository.jpa.entity.IdDocumentEntity;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.acme.onboarding.outbound.id_document.repository.jpa.entity.IdDocumentEntity.*;

@ApplicationScoped
public class IdDocumentRepositoryImpl implements IdDocumentRepository {
    private final EntityManager em;
    private final ManagedExecutor executor;

    @Inject
    public IdDocumentRepositoryImpl(EntityManager em, ManagedExecutor executor) {
        this.em = em;
        this.executor = executor;
    }

    @Override
    public CompletionStage<Void> persistDocumentData(ProcessedDocument idDocument, Username username, Email email) {
        return runAsync(() -> persistDocumentSync(idDocument, username, email), executor);
    }

    @Override
    @Transactional
    public List<Email> findEmailsForExpiredDocuments() {
        return em.createNamedQuery(FIND_EMAILS_FOR_EXPIRED_DOCUMENTS_QUERY, String.class)
                .getResultStream()
                .map(Email::new)
                .toList();
    }

    @Transactional
    void persistDocumentSync(ProcessedDocument idDocument, Username username, Email email) {
        em.createNamedQuery(FIND_BY_USERNAME_QUERY, IdDocumentEntity.class)
                .setParameter(USERNAME_PARAM, username.value().toUpperCase())
                .getResultStream()
                .findFirst()
                .ifPresentOrElse(
                        found -> em.merge(mapEntity(found, idDocument, email)),
                        () -> em.persist(mapNewRecord(idDocument, username, email)));
    }

    private IdDocumentEntity mapNewRecord(ProcessedDocument idDocument, Username username, Email email) {
        return new IdDocumentEntity(
                username.value().toUpperCase(),
                email.value().toLowerCase(),
                idDocument.fiscalCode().value(),
                idDocument.idDocumentNumber().value().toUpperCase(),
                idDocument.idDocumentType(),
                idDocument.issuingDate(),
                idDocument.expiringDate(),
                idDocument.documentUrl().value().toLowerCase());
    }

    private IdDocumentEntity mapEntity(IdDocumentEntity foundEntity, ProcessedDocument idDocument, Email email) {
        foundEntity.setIdDocumentExpiringDate(idDocument.expiringDate());
        foundEntity.setIdDocumentNumber(idDocument.idDocumentNumber().value().toUpperCase());
        foundEntity.setIdDocumentIssuingDate(idDocument.issuingDate());
        foundEntity.setIdDocumentType(idDocument.idDocumentType());
        foundEntity.setUpdateDatetime(Instant.now());
        foundEntity.setEmail(email.value().toLowerCase());

        return foundEntity;
    }
}
