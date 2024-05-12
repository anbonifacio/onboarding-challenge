package org.acme.onboarding.outbound.ocr.repository.client.impl;

import jakarta.enterprise.context.RequestScoped;
import org.acme.onboarding.domain.model.id_document.IdDocumentType;
import org.acme.onboarding.outbound.ocr.repository.client.OcrClient;
import org.acme.onboarding.outbound.ocr.repository.client.model.IdDocumentRequest;
import org.acme.onboarding.outbound.ocr.repository.client.model.ProcessedDocumentResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.instancio.Instancio;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedStage;
import static java.util.concurrent.CompletableFuture.failedStage;

@RequestScoped
@RegisterRestClient
public class OcrClientMockImpl implements OcrClient {
    @Override
    public CompletionStage<ProcessedDocumentResponse> processDocument(IdDocumentRequest request) {
        if (Math.random() < 0.2) {
            return failedStage(new RuntimeException("Processed document failed"));
        }
        return completedStage(Instancio.of(ProcessedDocumentResponse.Builder.class)
                .create()
                .withIdDocumentType(Instancio.of(IdDocumentType.class).create().name())
                .build());
    }
}
