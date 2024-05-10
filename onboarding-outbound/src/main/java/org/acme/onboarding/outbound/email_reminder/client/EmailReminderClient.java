package org.acme.onboarding.outbound.email_reminder.client;

import org.acme.onboarding.outbound.email_reminder.client.model.EmailReminderRequest;

import java.util.concurrent.CompletionStage;

public interface EmailReminderClient {
    CompletionStage<Void> sendEmailReminders(EmailReminderRequest request);
}
