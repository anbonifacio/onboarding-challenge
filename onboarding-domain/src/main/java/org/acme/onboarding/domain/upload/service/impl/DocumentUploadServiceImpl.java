package org.acme.onboarding.domain.upload.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.domain.repository.IdDocumentRepository;
import org.acme.onboarding.domain.repository.OcrRepository;
import org.acme.onboarding.domain.upload.service.DocumentUploadService;

import java.io.File;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class DocumentUploadServiceImpl implements DocumentUploadService {
    private final OcrRepository ocrRepository;
    private final IdDocumentRepository idDocumentRepository;

    @Inject
    public DocumentUploadServiceImpl(OcrRepository ocrRepository, IdDocumentRepository idDocumentRepository) {
        this.ocrRepository = ocrRepository;
        this.idDocumentRepository = idDocumentRepository;
    }

    @Override
    public CompletionStage<Void> upload(File file, Username username, Email email) {

        return ocrRepository
                .processDocument(file)
                .thenCompose(processedDocument ->
                        idDocumentRepository.persistDocumentData(processedDocument, username, email));
    }
}
