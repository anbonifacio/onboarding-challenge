package org.acme.onboarding.outbound.ocr.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.exception.RepositoryException;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.repository.OcrRepository;
import org.acme.onboarding.domain.upload.service.mapper.Base64Mapper;
import org.acme.onboarding.outbound.ocr.repository.client.OcrClient;
import org.acme.onboarding.outbound.ocr.repository.client.mapper.ProcessedDocumentMapper;
import org.acme.onboarding.outbound.ocr.repository.client.model.IdDocumentRequest;

import java.io.File;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@ApplicationScoped
public class OcrRepositoryImpl implements OcrRepository {
    private final Logger log = Logger.getLogger(getClass().getName());

    private final OcrClient ocrClient;
    private final Base64Mapper base64Mapper;
    private final ProcessedDocumentMapper mapper;

    @Inject
    public OcrRepositoryImpl(OcrClient ocrClient, Base64Mapper base64Mapper, ProcessedDocumentMapper mapper) {
        this.ocrClient = ocrClient;
        this.base64Mapper = base64Mapper;
        this.mapper = mapper;
    }

    @Override
    public CompletionStage<ProcessedDocument> processDocument(File file) {
        return base64Mapper
                .map(file)
                .thenCompose(base64 -> ocrClient.processDocument(new IdDocumentRequest(base64)))
                .thenApply(mapper::map)
                .exceptionally(ex -> {
                    log.severe(ex::getMessage);
                    throw new RepositoryException("Failed to process document", ex);
                });
    }
}
