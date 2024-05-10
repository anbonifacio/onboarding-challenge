package org.acme.onboarding.outbound.ocr.repository.client.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.model.id_document.FiscalCode;
import org.acme.onboarding.domain.model.id_document.IdDocumentNumber;
import org.acme.onboarding.domain.model.id_document.IdDocumentUrl;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.outbound.ocr.repository.client.model.ProcessedDocumentResponse;

@ApplicationScoped
public class ProcessedDocumentMapper {
    private final IdDocumentTypeMapper typeMapper;

    @Inject
    public ProcessedDocumentMapper(IdDocumentTypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    public ProcessedDocument map(ProcessedDocumentResponse response) {
        return new ProcessedDocument(
                new FiscalCode(response.fiscalCode()),
                new IdDocumentNumber(response.idDocumentNumber()),
                typeMapper.map(response.idDocumentType()),
                response.issuingDate(),
                response.expiringDate(),
                new IdDocumentUrl(response.idDocumentUrl()));
    }
}
