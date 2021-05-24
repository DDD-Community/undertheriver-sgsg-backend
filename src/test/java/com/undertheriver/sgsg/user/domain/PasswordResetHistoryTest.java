package com.undertheriver.sgsg.user.domain;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordResetHistoryTest {

    @DisplayName("유효시간이 지나면 true 아니면 false를 출력한다")
    @CsvSource(value = {"-10,true", "10,false"})
    @ParameterizedTest
    void isExpired(long weight, boolean expect) {
        PasswordResetHistory history = PasswordResetHistory.builder()
            .expiredAt(now())
            .initializationCredential("TEST")
            .userId(1L)
            .build();

        LocalDateTime currentTime = now().plusMinutes(weight);

        assertThat(history.isExpired(currentTime)).isEqualTo(expect);
    }
}