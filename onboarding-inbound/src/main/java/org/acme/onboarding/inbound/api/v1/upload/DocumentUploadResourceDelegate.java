package org.acme.onboarding.inbound.api.v1.upload;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.acme.onboarding.domain.model.Username;
import org.acme.onboarding.domain.upload.service.DocumentUploadService;
import org.acme.onboarding.inbound.annotation.InboundDelegate;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@RequestScoped
@InboundDelegate
public class DocumentUploadResourceDelegate implements DocumentUploadResource {
    private final Logger log = Logger.getLogger(getClass().getName());
    private final DocumentUploadService service;

    @Inject
    public DocumentUploadResourceDelegate(DocumentUploadService service) {
        this.service = service;
    }

    @Override
    public CompletionStage<Void> upload(FileUpload file, Username username) {
        return service.upload(file.uploadedFile().toFile(), username);
    }
}
