package org.acme.onboarding.inbound.exception;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import org.acme.onboarding.domain.exception.RepositoryException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.concurrent.CompletionException;

@RequestScoped
public class CompletionExceptionMapper {
    @ServerExceptionMapper
    public RestResponse<InboundError> mapException(CompletionException ex) {
        return switch (ex.getCause()) {
            case RepositoryException r -> RestResponse.status(
                    Response.Status.BAD_REQUEST, new InboundError(r.getMessage()));
            default -> RestResponse.status(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    new InboundError(ex.getCause().getMessage()));
        };
    }
}
