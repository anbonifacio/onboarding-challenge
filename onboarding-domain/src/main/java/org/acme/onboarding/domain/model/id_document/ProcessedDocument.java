package org.acme.onboarding.domain.model.id_document;

import java.net.URL;
import java.time.LocalDate;

public record ProcessedDocument(
        FiscalCode fiscalCode,
        IdDocumentNumber idDocumentNumber,
        IdDocumentType idDocumentType,
        LocalDate issuingDate,
        LocalDate expiringDate,
        URL documentUrl) {}
