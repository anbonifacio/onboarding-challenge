package org.acme.onboarding.inbound.api.v1.upload;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.domain.upload.service.DocumentUploadService;
import org.acme.onboarding.inbound.annotation.InboundDelegate;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.concurrent.CompletionStage;

@RequestScoped
@InboundDelegate
public class DocumentUploadResourceDelegate implements DocumentUploadResource {
    private final DocumentUploadService service;

    @Inject
    public DocumentUploadResourceDelegate(DocumentUploadService service) {
        this.service = service;
    }

    @Override
    public CompletionStage<Void> upload(FileUpload file, Username username, Email email) {
        return service.upload(file.uploadedFile().toFile(), username, email);
    }
}
