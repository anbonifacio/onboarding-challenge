package org.acme.onboarding.outbound.email_reminder.repository.client.mapper;

import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.outbound.email_reminder.repository.client.model.EmailReminderRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmailReminderRequestMapperTest {

    private final EmailReminderRequestMapper sut = new EmailReminderRequestMapper();

    @Test
    void shouldMap() {
        // given
        var emails = Instancio.createList(Email.class);
        var mapped = emails.stream().map(Email::value).toList();

        // when
        var result = sut.map(emails);

        // then
        assertThat(result).isEqualTo(new EmailReminderRequest(mapped));
    }
}
