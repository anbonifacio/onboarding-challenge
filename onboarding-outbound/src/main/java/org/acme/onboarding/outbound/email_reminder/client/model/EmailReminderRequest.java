package org.acme.onboarding.outbound.email_reminder.client.model;

import java.util.List;

public record EmailReminderRequest(List<String> recipients) {}
