package org.acme.onboarding.outbound.ocr.repository.client.impl;

import jakarta.enterprise.context.RequestScoped;
import org.acme.onboarding.outbound.ocr.repository.client.OcrClient;
import org.acme.onboarding.outbound.ocr.repository.client.model.IdDocumentRequest;
import org.acme.onboarding.outbound.ocr.repository.client.model.ProcessedDocumentResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.LocalDate;
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
        return completedStage(new ProcessedDocumentResponse(
                "BNFNTN85E06L738I",
                "123456789",
                "PASSPORT",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusYears(5),
                "https://google.it"));
    }
}
