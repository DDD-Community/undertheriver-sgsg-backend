package com.undertheriver.sgsg.user.domain;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordResetHistoryTest {

    @DisplayName("유효시간이 지나면 true 아니면 false를 출력한다")
    @CsvSource(value = {"2000-01-01T11:50:55, true", "2100-05-05T11:50:55, false"})
    @ParameterizedTest
    void isExpired(String date, boolean expect) {
        PasswordResetHistory history = PasswordResetHistory.builder()
            .expiredAt(LocalDateTime.parse(date))
            .initializationCredential("TEST")
            .userId(1L)
            .build();

        assertThat(history.isExpired(now())).isEqualTo(expect);
    }
}