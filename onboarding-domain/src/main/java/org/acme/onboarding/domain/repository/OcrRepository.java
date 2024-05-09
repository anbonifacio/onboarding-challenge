package org.acme.onboarding.domain.repository;

import org.acme.onboarding.domain.model.id_document.ProcessedDocument;

import java.io.File;
import java.util.concurrent.CompletionStage;

public interface OcrRepository {
    CompletionStage<ProcessedDocument> processDocument(File file);
}
