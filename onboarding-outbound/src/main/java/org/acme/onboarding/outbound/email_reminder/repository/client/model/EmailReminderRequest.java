package org.acme.onboarding.outbound.email_reminder.repository.client.model;

import java.util.List;

public record EmailReminderRequest(List<String> recipients) {}
