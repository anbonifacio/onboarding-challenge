package org.acme.onboarding.inbound.annotation;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
@Target({PARAMETER, TYPE, FIELD})
public @interface InboundDelegate {}
