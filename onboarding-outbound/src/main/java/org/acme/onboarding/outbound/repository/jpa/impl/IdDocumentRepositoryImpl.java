package org.acme.onboarding.outbound.repository.jpa.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.domain.repository.IdDocumentRepository;
import org.acme.onboarding.outbound.repository.jpa.entity.IdDocumentEntity;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.time.Instant;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.acme.onboarding.outbound.repository.jpa.entity.IdDocumentEntity.FIND_BY_USERNAME;
import static org.acme.onboarding.outbound.repository.jpa.entity.IdDocumentEntity.USERNAME;

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
        return runAsync(() -> persistDocument(idDocument, username, email), executor);
    }

    @Transactional
    public void persistDocument(ProcessedDocument idDocument, Username username, Email email) {
        em.createNamedQuery(FIND_BY_USERNAME, IdDocumentEntity.class)
                .setParameter(USERNAME, username.value().toUpperCase())
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
