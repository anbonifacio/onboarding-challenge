package org.acme.onboarding.outbound.ocr.repository.client.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.onboarding.domain.exception.RepositoryException;
import org.acme.onboarding.domain.model.id_document.IdDocumentType;

@ApplicationScoped
public class IdDocumentTypeMapper {
    public IdDocumentType map(String documentType) {
        return switch (documentType) {
            case "ID_CARD" -> IdDocumentType.ID_CARD;
            case "DRIVING_LICENSE" -> IdDocumentType.DRIVING_LICENSE;
            case "PASSPORT" -> IdDocumentType.PASSPORT;
                // FIXME: Java 21 supports "case null, default ->" but breaks palantir-java-formatter
            case null -> throw new RepositoryException(new IllegalStateException("Null value"));
            default -> throw new RepositoryException(new IllegalStateException("Unexpected value: " + documentType));
        };
    }
}
