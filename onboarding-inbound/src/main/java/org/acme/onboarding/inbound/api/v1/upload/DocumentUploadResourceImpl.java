package org.acme.onboarding.inbound.api.v1.upload;

import io.quarkus.security.PermissionsAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;
import org.acme.onboarding.domain.model.Username;
import org.acme.onboarding.inbound.annotation.InboundDelegate;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@RequestScoped
@Path("/api/v1/documents")
public class DocumentUploadResourceImpl implements DocumentUploadResource {
    private final Logger log = Logger.getLogger(getClass().getName());
    private final JsonWebToken jsonWebToken;
    private final DocumentUploadResource delegate;

    @Inject
    public DocumentUploadResourceImpl(JsonWebToken jsonWebToken, @InboundDelegate DocumentUploadResource delegate) {
        this.jsonWebToken = jsonWebToken;
        this.delegate = delegate;
    }

    @Override
    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @PermissionsAllowed("email")
    public CompletionStage<Void> upload(@RestForm("image") FileUpload file, Username unused) {
        var email = jsonWebToken.getClaim(Claims.email.name()).toString();
        var username = jsonWebToken.getClaim(Claims.preferred_username.name()).toString();
        log.info(() -> "Getting the username '%s' and email '%s' from the JWT".formatted(username, email));
        return delegate.upload(file, new Username(username));
    }
}
