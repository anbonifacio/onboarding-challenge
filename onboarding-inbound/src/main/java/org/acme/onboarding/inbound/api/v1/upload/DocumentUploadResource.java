package org.acme.onboarding.inbound.api.v1.upload;

import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.concurrent.CompletionStage;

public interface DocumentUploadResource {
    CompletionStage<Void> upload(FileUpload file, Username username, Email email);
}
