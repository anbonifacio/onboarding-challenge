package org.acme.onboarding.outbound.id_document.repository.jpa.impl;

import jakarta.persistence.EntityManager;
import org.acme.onboarding.domain.model.id_document.ProcessedDocument;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.outbound.id_document.repository.jpa.entity.IdDocumentEntity;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdDocumentRepositoryImplTest {
    private IdDocumentRepositoryImpl sut;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private EntityManager em;

    @Mock
    private ManagedExecutor executor;

    @BeforeEach
    void setUp() {
        sut = new IdDocumentRepositoryImpl(em, executor);
        lenient()
                .doAnswer((Answer<CompletionStage<String>>) invocation -> {
                    Object[] args = invocation.getArguments();
                    Runnable runnable = (Runnable) args[0];
                    runnable.run();
                    return null;
                })
                .when(executor)
                .execute(any(Runnable.class));
    }

    @Test
    void shouldPersistDocumentData() {
        // given
        var username = Instancio.of(Username.class).create();
        var email = Instancio.of(Email.class).create();
        var processedDocument = Instancio.of(ProcessedDocument.class).create();
        var entity = Instancio.of(IdDocumentEntity.class).create();
        var query = em.createNamedQuery(IdDocumentEntity.FIND_BY_USERNAME_QUERY, IdDocumentEntity.class)
                .setParameter(IdDocumentEntity.USERNAME_PARAM, username.value());
        given(query.getResultList()).willReturn(List.of(entity));

        // when
        var result = sut.persistDocumentData(processedDocument, username, email);

        // then
        assertThat(result).isCompleted();
    }

    @Test
    void shouldFindEmailsForExpiredDocuments() {
        // given
        var emails = Instancio.createList(String.class);
        var query = em.createNamedQuery(IdDocumentEntity.FIND_EMAILS_FOR_EXPIRED_DOCUMENTS_QUERY, String.class);
        given(query.getResultStream()).willReturn(emails.stream());

        // when
        var result = sut.findEmailsForExpiredDocuments();
        var expected = emails.stream().map(Email::new).toList();

        // then
        assertThat(result).containsExactlyElementsOf(expected);
        verifyNoMoreInteractions(query);
        verifyNoInteractions(executor);
    }

    @Test
    void shouldPersistDocumentSync() {
        // given
        var username = Instancio.of(Username.class).create();
        var email = Instancio.of(Email.class).create();
        var processedDocument = Instancio.of(ProcessedDocument.class).create();
        var entity = Instancio.of(IdDocumentEntity.class).create();
        var query = em.createNamedQuery(IdDocumentEntity.FIND_BY_USERNAME_QUERY, IdDocumentEntity.class)
                .setParameter(IdDocumentEntity.USERNAME_PARAM, username.value());
        given(query.getResultList()).willReturn(List.of(entity));

        // when
        // then
        assertThatNoException().isThrownBy(() -> sut.persistDocumentSync(processedDocument, username, email));
        verifyNoInteractions(executor);
    }
}
