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
        return ProcessedDocument.Builder.builder()
                .withFiscalCode(new FiscalCode(response.fiscalCode()))
                .withIdDocumentNumber(new IdDocumentNumber(response.idDocumentNumber()))
                .withIdDocumentType(typeMapper.map(response.idDocumentType()))
                .withIssuingDate(response.issuingDate())
                .withExpiringDate(response.expiringDate())
                .withDocumentUrl(new IdDocumentUrl(response.idDocumentUrl()))
                .build();
    }
}
