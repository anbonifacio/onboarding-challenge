package org.acme.onboarding.outbound.ocr.repository.client.model;

import java.time.LocalDate;

public record ProcessedDocumentResponse(
        String fiscalCode,
        String idDocumentNumber,
        String idDocumentType,
        LocalDate issuingDate,
        LocalDate expiringDate,
        String idDocumentUrl) {}
