package org.acme.onboarding.outbound.ocr.repository.client.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.onboarding.domain.model.id_document.IdDocumentType;

@ApplicationScoped
public class IdDocumentTypeMapper {
    public IdDocumentType map(String documentType) {
        return switch (documentType) {
            case "ID_CARD" -> IdDocumentType.ID_CARD;
            case "DRIVING_LICENSE" -> IdDocumentType.DRIVING_LICENSE;
            case "PASSPORT" -> IdDocumentType.PASSPORT;
            default -> throw new IllegalStateException("Unexpected value: " + documentType);
        };
    }
}