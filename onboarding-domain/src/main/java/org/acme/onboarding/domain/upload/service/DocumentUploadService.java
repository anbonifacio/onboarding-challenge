package org.acme.onboarding.domain.upload.service;

import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;

import java.io.File;
import java.util.concurrent.CompletionStage;

public interface DocumentUploadService {
    CompletionStage<Void> upload(File file, Username username, Email email);
}
