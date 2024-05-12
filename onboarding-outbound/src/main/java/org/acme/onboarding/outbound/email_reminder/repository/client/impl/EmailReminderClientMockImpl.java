package org.acme.onboarding.outbound.email_reminder.repository.client.impl;

import jakarta.enterprise.context.RequestScoped;
import org.acme.onboarding.outbound.email_reminder.repository.client.EmailReminderClient;
import org.acme.onboarding.outbound.email_reminder.repository.client.model.EmailReminderRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

import static java.util.concurrent.CompletableFuture.completedStage;

@RequestScoped
@RegisterRestClient
public class EmailReminderClientMockImpl implements EmailReminderClient {
    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public CompletionStage<Void> sendEmailReminders(EmailReminderRequest request) {
        log.info(() -> "Sending email reminder request: %s".formatted(request));
        return completedStage(null);
    }
}
