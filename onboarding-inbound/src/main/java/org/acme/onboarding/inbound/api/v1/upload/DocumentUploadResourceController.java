package org.acme.onboarding.inbound.api.v1.upload;

import io.quarkus.security.PermissionsAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;
import org.acme.onboarding.domain.model.user.Email;
import org.acme.onboarding.domain.model.user.Username;
import org.acme.onboarding.inbound.annotation.InboundDelegate;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

import static java.util.concurrent.CompletableFuture.failedStage;
import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@Path("/api/v1/documents")
@Tag(name = "upload", description = "Upload ID document")
@SecurityRequirement(name = "Bearer token")
@SecurityScheme(
        description = "Header name: Authorization",
        securitySchemeName = "Bearer token",
        scheme = "bearer",
        bearerFormat = "bearer",
        type = HTTP)
public class DocumentUploadResourceController {
    private final Logger log = Logger.getLogger(getClass().getName());
    private final JsonWebToken jsonWebToken;
    private final DocumentUploadResource delegate;

    @Inject
    public DocumentUploadResourceController(
            JsonWebToken jsonWebToken, @InboundDelegate DocumentUploadResource delegate) {
        this.jsonWebToken = jsonWebToken;
        this.delegate = delegate;
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @PermissionsAllowed("email")
    @Operation(operationId = "uploadDocument")
    @APIResponse(
            responseCode = "204",
            name = "204",
            description = "Document uploaded",
            content = {@Content(mediaType = "application/json")})
    @APIResponse(
            responseCode = "401",
            name = "401",
            description = "Unauthorized",
            content = {@Content(mediaType = "application/json")})
    @APIResponse(
            responseCode = "403",
            name = "403",
            description = "Forbidden",
            content = {@Content(mediaType = "application/json")})
    @APIResponse(
            responseCode = "404",
            name = "404",
            description = "Bad Request",
            content = {@Content(mediaType = "application/json")})
    @APIResponse(
            responseCode = "500",
            name = "500",
            description = "Internal error",
            content = {@Content(mediaType = "application/json")})
    public CompletionStage<Void> upload(
            @RequestBody(
                            content =
                                    @Content(
                                            mediaType = "multipart/form-data",
                                            schema =
                                                    @Schema(
                                                            type = SchemaType.OBJECT,
                                                            description =
                                                                    "multipart form. multiple files can be passed")))
                    @RestForm("image")
                    FileUpload file) {
        if (jsonWebToken.getClaim(Claims.email_verified.name())) {
            if (file == null) {
                return failedStage(new BadRequestException("Missing file"));
            }
            var email = jsonWebToken.getClaim(Claims.email.name()).toString();
            var username =
                    jsonWebToken.getClaim(Claims.preferred_username.name()).toString();
            log.info(() -> "Getting the username '%s' and email '%s' from the JWT".formatted(username, email));
            return delegate.upload(file, new Username(username), new Email(email));
        } else {
            return failedStage(new ForbiddenException("Not authorized"));
        }
    }
}
