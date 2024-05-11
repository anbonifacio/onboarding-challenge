package org.acme.onboarding.domain.upload.service.impl;

import org.acme.onboarding.domain.exception.RepositoryException;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.domain.repository.IdDocumentRepository;
import org.acme.onboarding.domain.repository.OcrRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.completedStage;
import static java.util.concurrent.CompletableFuture.failedStage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DocumentUploadServiceImplTest {
    private DocumentUploadServiceImpl sut;

    @Mock
    private OcrRepository ocrRepository;

    @Mock
    private IdDocumentRepository idDocumentRepository;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadServiceImpl(ocrRepository, idDocumentRepository);
    }

    @Test
    void shouldUploadDocument() {
        // given
        var file = Instancio.of(File.class).create();
        var processedDoc = Instancio.of(ProcessedDocument.class).create();
        var username = Instancio.of(Username.class).create();
        var email = Instancio.of(Email.class).create();

        given(ocrRepository.processDocument(file)).willReturn(completedStage(processedDoc));
        given(idDocumentRepository.persistDocumentData(processedDoc, username, email))
                .willReturn(completedStage(null));

        // when
        var result = sut.upload(file, username, email);

        // then
        assertThat(result).isCompleted();
        verifyNoMoreInteractions(ocrRepository, idDocumentRepository);
    }

    @Test
    void shouldThrowExceptionIfOCRFails() {
        // given
        var file = Instancio.of(File.class).create();
        var username = Instancio.of(Username.class).create();
        var email = Instancio.of(Email.class).create();

        given(ocrRepository.processDocument(file)).willReturn(failedStage(new RuntimeException()));

        // when
        var result = sut.upload(file, username, email);

        // then
        assertThat(result)
                .failsWithin(2L, TimeUnit.SECONDS)
                .withThrowableThat()
                .withCauseInstanceOf(RepositoryException.class);
        verifyNoMoreInteractions(ocrRepository);
        verifyNoInteractions(idDocumentRepository);
    }

    @Test
    void shouldThrowExceptionIfIdDocumentRepositoryFails() {
        // given
        var file = Instancio.of(File.class).create();
        var processedDoc = Instancio.of(ProcessedDocument.class).create();
        var username = Instancio.of(Username.class).create();
        var email = Instancio.of(Email.class).create();

        given(ocrRepository.processDocument(file)).willReturn(completedStage(processedDoc));
        given(idDocumentRepository.persistDocumentData(processedDoc, username, email))
                .willReturn(failedStage(new RuntimeException()));

        // when
        var result = sut.upload(file, username, email);

        // then
        assertThat(result)
                .failsWithin(2L, TimeUnit.SECONDS)
                .withThrowableThat()
                .withCauseInstanceOf(RepositoryException.class);
        verifyNoMoreInteractions(ocrRepository, idDocumentRepository);
    }
}
