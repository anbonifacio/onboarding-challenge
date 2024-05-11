package org.acme.onboarding.domain.upload.service.mapper;

import io.github.anbonifacio.try_monad.Try;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class Base64Mapper {
    public CompletionStage<String> map(File file) {
        return Try.of(() -> Files.readAllBytes(file.toPath()))
                .map(bytes -> Base64.getEncoder().encodeToString(bytes))
                .toCompletionStage();
    }
}
