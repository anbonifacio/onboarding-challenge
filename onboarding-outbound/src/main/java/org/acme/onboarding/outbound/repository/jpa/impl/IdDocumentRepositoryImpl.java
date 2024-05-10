package org.acme.onboarding.outbound.repository.jpa.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.domain.repository.IdDocumentRepository;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedStage;

@ApplicationScoped
public class IdDocumentRepositoryImpl implements IdDocumentRepository {
    @Override
    public CompletionStage<Void> persistDocumentData(ProcessedDocument idDocument, Username username, Email email) {
        return completedStage(null);
    }
}
