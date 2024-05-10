package org.acme.onboarding.outbound.ocr.repository.client.impl;

import jakarta.enterprise.context.RequestScoped;
import org.acme.onboarding.outbound.ocr.repository.client.OcrClient;
import org.acme.onboarding.outbound.ocr.repository.client.model.IdDocumentRequest;
import org.acme.onboarding.outbound.ocr.repository.client.model.ProcessedDocumentResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedStage;

@RequestScoped
@RegisterRestClient
public class OcrClientMockImpl implements OcrClient {
    @Override
    public CompletionStage<ProcessedDocumentResponse> processDocument(IdDocumentRequest request) {
        return completedStage(new ProcessedDocumentResponse(
                "",
                "",
                "PASSPORT",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusYears(5),
                ""));
    }
}
