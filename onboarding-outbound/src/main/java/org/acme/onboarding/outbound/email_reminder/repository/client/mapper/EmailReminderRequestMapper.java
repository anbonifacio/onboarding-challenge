package org.acme.onboarding.outbound.email_reminder.repository.client.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.outbound.email_reminder.repository.client.model.EmailReminderRequest;

import java.util.List;

@ApplicationScoped
public class EmailReminderRequestMapper {
    public EmailReminderRequest map(List<Email> emails) {
        var req = emails.stream().map(Email::value).toList();
        return new EmailReminderRequest(req);
    }
}
