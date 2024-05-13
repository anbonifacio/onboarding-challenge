# onboarding-challenge

This project uses Quarkus to build a service that allows users to upload an ID document image, 
in order to be processed from an external OCR.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
mvn clean compile quarkus:dev
```
This will also start testcontainers for external services like Keycloak and Postgresql.

To run the tests: 
```shell script
mvn clean verify
```

> **_NOTE:_**  This project uses my library [java-try-monad](https://github.com/anbonifacio/java-try-monad) from its GitHub maven repo .
> In order to install it you must have a GitHub `read:packages` token configured in .m2/settings.xml or clone and `mvn install` the git repo locally.

## Testing the application

After the project is started, a swagger-ui can be accessed at http://localhost:8080/q/swagger-ui.
In order to do some test calls, you need to generate a Bearer token from Keycloak using the Quarkus DevUI at http://localhost:8080/q/dev-ui/io.quarkus.quarkus-oidc/keycloak-provider.

Keycloak is preconfigured with a realm with 2 users, `alice:alice` and `jdoe:jdoe`. User alice can use the application,
since she has a valid email. User jdoe should give 403.

A background task is set to run every 10s and queries the db for expired documents in order to send email notifications.
It writes logs only if expired documents are found.

## Code structure

The project is organized with a multimodule structure and follows the "Hexagonal" Architectural Pattern: 
- the `onboarding-domain` module has non dependencies on other modules and contains the domain logic for the application.
- the `onboarding-inbound` module depends on domain and is the adapter for the incoming requests.
- the `onboarding-outbound` module depends on domain and is the adapter to the database for persistence and to the external services, like OCR and emailer. It also  contains the emailer task.
- the `onboarding` module is the wrapper module that depends on all other and represents the starting point for the applications. It also contains app configurations and integration tests.

Other patterns I used in the project:
- newtype pattern (using Java records)
- builder pattern
- repository pattern
