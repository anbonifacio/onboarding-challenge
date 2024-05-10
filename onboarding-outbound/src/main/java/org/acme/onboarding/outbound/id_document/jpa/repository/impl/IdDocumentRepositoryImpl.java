package org.acme.onboarding.outbound.id_document.jpa.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.domain.repository.IdDocumentRepository;
import org.acme.onboarding.outbound.id_document.jpa.repository.entity.IdDocumentEntity;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.acme.onboarding.outbound.id_document.jpa.repository.entity.IdDocumentEntity.*;

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
    public void persistDocumentSync(ProcessedDocument idDocument, Username username, Email email) {
        em.createNamedQuery(FIND_BY_USERNAME_QUERY, IdDocumentEntity.class)
                .setParameter(USERNAME_PARAM, username.value().toUpperCase())
                .getResultStream()
                .findFirst()
                .ifPresentOrElse(
                        found -> {
                            found.setIdDocumentExpiringDate(idDocument.expiringDate());
                            found.setIdDocumentNumber(
                                    idDocument.idDocumentNumber().value().toLowerCase());
                            found.setIdDocumentIssuingDate(idDocument.issuingDate());
                            found.setIdDocumentType(idDocument.idDocumentType());
                            found.setUpdateDatetime(Instant.now());
                            found.setEmail(email.value().toLowerCase());
                            em.merge(found);
                        },
                        () -> {
                            var newRecord = new IdDocumentEntity(
                                    username.value().toUpperCase(),
                                    email.value().toLowerCase(),
                                    idDocument.fiscalCode().value(),
                                    idDocument.idDocumentNumber().value().toUpperCase(),
                                    idDocument.idDocumentType(),
                                    idDocument.issuingDate(),
                                    idDocument.expiringDate(),
                                    idDocument.documentUrl().value().toLowerCase());
                            em.persist(newRecord);
                        });
    }
}
