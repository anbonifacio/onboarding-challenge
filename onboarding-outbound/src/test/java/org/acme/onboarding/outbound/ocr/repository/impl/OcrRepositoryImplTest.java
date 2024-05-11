package org.acme.onboarding.outbound.ocr.repository.impl;

import org.acme.onboarding.domain.exception.RepositoryException;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.upload.service.mapper.Base64Mapper;
import org.acme.onboarding.outbound.ocr.repository.client.OcrClient;
import org.acme.onboarding.outbound.ocr.repository.client.mapper.ProcessedDocumentMapper;
import org.acme.onboarding.outbound.ocr.repository.client.model.IdDocumentRequest;
import org.acme.onboarding.outbound.ocr.repository.client.model.ProcessedDocumentResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class OcrRepositoryImplTest {

    private OcrRepositoryImpl sut;

    @Mock
    private OcrClient ocrClient;

    @Mock
    private Base64Mapper base64Mapper;

    @Mock
    private ProcessedDocumentMapper mapper;

    @BeforeEach
    void setUp() {
        sut = new OcrRepositoryImpl(ocrClient, base64Mapper, mapper);
    }

    @Test
    void shouldProcessDocument() {
        // given
        var file = Instancio.of(File.class).create();
        var request = Instancio.of(IdDocumentRequest.class).create();
        var response = Instancio.of(ProcessedDocumentResponse.class).create();
        var expected = Instancio.of(ProcessedDocument.class).create();

        given(base64Mapper.map(file)).willReturn(completedStage(request.base64Document()));
        given(ocrClient.processDocument(request)).willReturn(completedStage(response));
        given(mapper.map(response)).willReturn(expected);

        // when
        var result = sut.processDocument(file);

        // then
        assertThat(result).isCompletedWithValue(expected);
        verifyNoMoreInteractions(base64Mapper, mapper, ocrClient);
    }

    @Test
    void shouldFailWhenBase64MapperFails() {
        // given
        var file = Instancio.of(File.class).create();

        given(base64Mapper.map(file)).willReturn(failedStage(new RepositoryException("failure")));

        // when
        var result = sut.processDocument(file);

        // then
        assertThat(result)
                .failsWithin(2, TimeUnit.SECONDS)
                .withThrowableThat()
                .withCauseInstanceOf(RepositoryException.class);
        verifyNoMoreInteractions(base64Mapper);
        verifyNoInteractions(mapper, ocrClient);
    }

    @Test
    void shouldFailWhenOcrClientFails() {
        // given
        var file = Instancio.of(File.class).create();
        var request = Instancio.of(IdDocumentRequest.class).create();

        given(base64Mapper.map(file)).willReturn(completedStage(request.base64Document()));
        given(ocrClient.processDocument(request)).willReturn(failedStage(new RepositoryException("failure")));

        // when
        var result = sut.processDocument(file);

        // then
        assertThat(result)
                .failsWithin(2, TimeUnit.SECONDS)
                .withThrowableThat()
                .withCauseInstanceOf(RepositoryException.class);

        verifyNoMoreInteractions(base64Mapper, ocrClient);
        verifyNoInteractions(mapper);
    }
}
