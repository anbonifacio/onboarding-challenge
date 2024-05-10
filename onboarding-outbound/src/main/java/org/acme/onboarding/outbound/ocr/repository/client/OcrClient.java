package org.acme.onboarding.outbound.ocr.repository.client;

import org.acme.onboarding.outbound.ocr.repository.client.model.IdDocumentRequest;
import org.acme.onboarding.outbound.ocr.repository.client.model.ProcessedDocumentResponse;

import java.util.concurrent.CompletionStage;

public interface OcrClient {
    CompletionStage<ProcessedDocumentResponse> processDocument(IdDocumentRequest request);
}
