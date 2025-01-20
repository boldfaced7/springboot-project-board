package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.CheckEmailDuplicationCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.boldfaced7.MemberTestUtil.EMAIL;

class CheckEmailDuplicationServiceTest {

    @DisplayName("이메일 정보를 보내면, 중복 여부를 반환")
    @ParameterizedTest
    @MethodSource
    void givenEmail_whenCheckEmailDuplication_thenReturnsBoolean(boolean isDuplicated) {
        // given
        var sut = new CheckEmailDuplicationService(
                command -> isDuplicated
        );
        var command = new CheckEmailDuplicationCommand(EMAIL);

        // when
        var result = sut.isDuplicated(command);

        // then
        Assertions.assertThat(result).isEqualTo(isDuplicated);
    }
    static Stream<Arguments> givenEmail_whenCheckEmailDuplication_thenReturnsBoolean() {
        return Stream.of(
                Arguments.of(true),
                Arguments.of(false)
        );
    }
}