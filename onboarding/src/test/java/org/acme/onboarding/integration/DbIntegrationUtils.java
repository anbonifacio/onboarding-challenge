package org.acme.onboarding.integration;

import io.github.anbonifacio.try_monad.Try;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public class DbIntegrationUtils {
    private DbIntegrationUtils() {}

    public static int runSqlScript(String sqlScript, EntityManager entityManager) {
        AtomicInteger affectedRows = new AtomicInteger();
        Arrays.stream(sqlScript.split(";", -1))
                .forEach(cmd -> affectedRows.addAndGet(tryToRunCommand(cmd, entityManager)));
        return affectedRows.get();
    }

    private static Integer tryToRunCommand(String command, EntityManager entityManager) {
        return Try.ofCallable(() -> entityManager.createNativeQuery(command).executeUpdate())
                .recoverWith(throwable -> Try.failure(new IllegalStateException(throwable)))
                .get();
    }

    public static String readFile(String fileName) {
        try (final var inputStream = DbIntegrationUtils.class.getResourceAsStream("/sql/" + fileName)) {
            Objects.requireNonNull(inputStream);
            return new BufferedReader(new InputStreamReader(inputStream, UTF_8))
                    .lines()
                    .collect(joining(" "));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
