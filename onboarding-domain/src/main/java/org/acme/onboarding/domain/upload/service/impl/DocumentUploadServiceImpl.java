package org.acme.onboarding.domain.upload.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.onboarding.domain.model.Username;
import org.acme.onboarding.domain.upload.service.DocumentUploadService;

import java.io.File;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedStage;

@ApplicationScoped
public class DocumentUploadServiceImpl implements DocumentUploadService {

    @Override
    public CompletionStage<Void> upload(File file, Username username) {
        return completedStage(null);
    }
}
