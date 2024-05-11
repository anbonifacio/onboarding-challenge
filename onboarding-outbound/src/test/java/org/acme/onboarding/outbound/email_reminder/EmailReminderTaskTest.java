package org.acme.onboarding.outbound.email_reminder;

import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.repository.EmailReminderRepository;
import org.acme.onboarding.domain.repository.IdDocumentRepository;
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

import static java.util.concurrent.CompletableFuture.completedStage;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailReminderTaskTest {

    private EmailReminderTask sut;

    @Mock
    private IdDocumentRepository documentRepository;

    @Mock
    private EmailReminderRepository emailReminderRepository;

    @Mock
    private ManagedExecutor executor;

    @BeforeEach
    void setUp() {
        sut = new EmailReminderTask(documentRepository, emailReminderRepository, executor);
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
    void shouldScheduleEmailReminder() {
        // given
        var emails = Instancio.createList(String.class).stream().map(Email::new).toList();
        given(documentRepository.findEmailsForExpiredDocuments()).willReturn(emails);
        given(emailReminderRepository.sendEmailReminders(emails)).willReturn(completedStage(null));

        // when
        assertThatNoException().isThrownBy(() -> sut.scheduleEmailReminder());

        // then
        verifyNoMoreInteractions(documentRepository, emailReminderRepository);
    }

    @Test
    void shouldNotScheduleEmailsWhenNoDocumentExpired() {
        // given
        List<Email> emails = List.of();
        given(documentRepository.findEmailsForExpiredDocuments()).willReturn(emails);

        // when
        assertThatNoException().isThrownBy(() -> sut.scheduleEmailReminder());

        // then
        verifyNoMoreInteractions(documentRepository);
        verifyNoInteractions(emailReminderRepository);
    }
}
