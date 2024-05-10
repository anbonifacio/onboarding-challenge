package org.acme.onboarding.outbound.email_reminder.client.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.outbound.email_reminder.client.model.EmailReminderRequest;

import java.util.List;

@ApplicationScoped
public class EmailReminderRequestMapper {
    public EmailReminderRequest map(List<Email> emails) {
        var req = emails.stream().map(email -> email.value()).toList();
        return new EmailReminderRequest(req);
    }
}
