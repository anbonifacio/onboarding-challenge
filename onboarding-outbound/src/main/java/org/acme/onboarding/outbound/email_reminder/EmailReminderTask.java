package org.acme.onboarding.outbound.email_reminder;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.repository.EmailReminderRepository;
import org.acme.onboarding.domain.repository.IdDocumentRepository;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.runAsync;

@ApplicationScoped
public class EmailReminderTask {
    private static final int BATCH_SIZE = 100;
    private final IdDocumentRepository documentRepository;
    private final EmailReminderRepository emailReminderRepository;
    private final ManagedExecutor executor;

    @Inject
    public EmailReminderTask(
            IdDocumentRepository documentRepository,
            EmailReminderRepository emailReminderRepository,
            ManagedExecutor executor) {
        this.documentRepository = documentRepository;
        this.emailReminderRepository = emailReminderRepository;
        this.executor = executor;
    }

    @Scheduled( // cron = "0 0 7 * * ?"
            every = "10s",
            concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void scheduleEmailReminder() {
        AtomicInteger counter = new AtomicInteger();
        var emails = documentRepository.findEmailsForExpiredDocuments().stream()
                .collect(Collectors.groupingBy(unused -> counter.getAndDecrement() / BATCH_SIZE))
                .values()
                .stream()
                .flatMap(Collection::stream)
                .toList();

        if (!emails.isEmpty()) {
            runAsync(() -> emailReminderRepository.sendEmailReminders(emails), executor);
        }
    }
}
