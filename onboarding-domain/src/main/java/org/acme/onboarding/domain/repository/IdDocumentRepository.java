package org.acme.onboarding.domain.repository;

import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Username;

import java.util.concurrent.CompletionStage;

public interface IdDocumentRepository {
    CompletionStage<Void> persistDocumentData(ProcessedDocument idDocument, Username username, Email email);
}
