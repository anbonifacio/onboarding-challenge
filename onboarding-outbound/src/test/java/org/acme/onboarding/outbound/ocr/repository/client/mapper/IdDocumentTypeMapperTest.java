package org.acme.onboarding.outbound.ocr.repository.client.mapper;

import org.acme.onboarding.domain.exception.RepositoryException;
import org.acme.onboarding.domain.model.id_document.IdDocumentType;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class IdDocumentTypeMapperTest {

    private final IdDocumentTypeMapper sut = new IdDocumentTypeMapper();

    @ParameterizedTest
    @EnumSource(IdDocumentType.class)
    void shouldMap(IdDocumentType idDocumentType) {
        // when
        var result = sut.map(idDocumentType.name());

        // then
        assertThat(result).isEqualTo(idDocumentType);
    }

    @ParameterizedTest
    @NullSource()
    void shouldThrowIfNull(String idDocumentType) {
        // when
        // then
        assertThatExceptionOfType(RepositoryException.class)
                .isThrownBy(() -> sut.map(idDocumentType))
                .withCauseInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIfInvalidType() {
        // given
        var idDocumentType = Instancio.of(String.class).create();

        // when
        // then
        assertThatExceptionOfType(RepositoryException.class)
                .isThrownBy(() -> sut.map(idDocumentType))
                .withCauseInstanceOf(IllegalStateException.class);
    }
}
