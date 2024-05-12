package org.acme.onboarding.outbound.ocr.repository.client.mapper;

import org.acme.onboarding.domain.model.id_document.*;
import org.acme.onboarding.outbound.ocr.repository.client.model.ProcessedDocumentResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ProcessedDocumentMapperTest {

    private ProcessedDocumentMapper sut;

    @Mock
    private IdDocumentTypeMapper typeMapper;

    @BeforeEach
    void setUp() {
        sut = new ProcessedDocumentMapper(typeMapper);
    }

    @Test
    void shouldMap() {
        // given
        var response = Instancio.of(ProcessedDocumentResponse.class).create();
        var idDocumentType = Instancio.of(IdDocumentType.class).create();
        var expected = ProcessedDocument.Builder.builder()
                .withFiscalCode(new FiscalCode(response.fiscalCode()))
                .withIdDocumentNumber(new IdDocumentNumber(response.idDocumentNumber()))
                .withIdDocumentType(idDocumentType)
                .withIssuingDate(response.issuingDate())
                .withExpiringDate(response.expiringDate())
                .withDocumentUrl(new IdDocumentUrl(response.idDocumentUrl()))
                .build();

        given(typeMapper.map(response.idDocumentType())).willReturn(idDocumentType);

        // when
        var result = sut.map(response);

        // then
        assertThat(result).isEqualTo(expected);
        verifyNoMoreInteractions(typeMapper);
    }
}
