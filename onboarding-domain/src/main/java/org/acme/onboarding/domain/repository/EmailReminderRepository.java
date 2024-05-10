package org.acme.onboarding.domain.repository;

import org.acme.onboarding.domain.model.user.Email;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface EmailReminderRepository {
    CompletionStage<Void> sendEmailReminders(List<Email> emailBatch);
}
