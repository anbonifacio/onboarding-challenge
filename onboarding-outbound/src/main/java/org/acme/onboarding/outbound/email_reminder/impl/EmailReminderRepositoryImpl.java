package org.acme.onboarding.outbound.email_reminder.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.repository.EmailReminderRepository;
import org.acme.onboarding.outbound.email_reminder.client.EmailReminderClient;
import org.acme.onboarding.outbound.email_reminder.client.mapper.EmailReminderRequestMapper;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class EmailReminderRepositoryImpl implements EmailReminderRepository {
    private final EmailReminderClient client;
    private final EmailReminderRequestMapper mapper;

    @Inject
    public EmailReminderRepositoryImpl(EmailReminderClient client, EmailReminderRequestMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public CompletionStage<Void> sendEmailReminders(List<Email> emails) {

        return client.sendEmailReminders(mapper.map(emails));
    }
}
