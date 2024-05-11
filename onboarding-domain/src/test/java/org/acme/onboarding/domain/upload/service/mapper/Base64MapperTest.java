package org.acme.onboarding.domain.upload.service.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class Base64MapperTest {

    private final Base64Mapper sut = new Base64Mapper();

    @ParameterizedTest
    @NullSource
    void shouldMapNullFileToFailedStage(File file) {
        // when
        var result = sut.map(file);

        // then
        assertThat(result)
                .failsWithin(2, TimeUnit.SECONDS)
                .withThrowableThat()
                .withCauseInstanceOf(NullPointerException.class);
    }
}
