package org.acme.onboarding.domain.di;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.Clock;

@ApplicationScoped
public class ClockProvider {

    @Produces
    public Clock produce() {

        return Clock.systemUTC();
    }
}
