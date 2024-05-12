package org.acme.onboarding.outbound.email_reminder.repository.client;

import org.acme.onboarding.outbound.email_reminder.repository.client.model.EmailReminderRequest;

import java.util.concurrent.CompletionStage;

public interface EmailReminderClient {
    CompletionStage<Void> sendEmailReminders(EmailReminderRequest request);
}
