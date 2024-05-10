package org.acme.onboarding.outbound.ocr.repository.impl;

import io.github.anbonifacio.try_monad.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.repository.OcrRepository;
import org.acme.onboarding.outbound.ocr.repository.client.OcrClient;
import org.acme.onboarding.outbound.ocr.repository.client.mapper.ProcessedDocumentMapper;
import org.acme.onboarding.outbound.ocr.repository.client.model.IdDocumentRequest;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@ApplicationScoped
public class OcrRepositoryImpl implements OcrRepository {
    private final Logger log = Logger.getLogger(getClass().getName());

    private final OcrClient ocrClient;
    private final ProcessedDocumentMapper mapper;

    @Inject
    public OcrRepositoryImpl(OcrClient ocrClient, ProcessedDocumentMapper mapper) {
        this.ocrClient = ocrClient;
        this.mapper = mapper;
    }

    @Override
    public CompletionStage<ProcessedDocument> processDocument(File file) {
        return Try.of(() -> Files.readAllBytes(file.toPath()))
                .map(Base64.getEncoder()::encodeToString)
                .toCompletionStage()
                .thenApply(base64 -> ocrClient.processDocument(new IdDocumentRequest(base64)))
                .thenCompose(processed -> processed.thenApply(mapper::map));
    }
}
