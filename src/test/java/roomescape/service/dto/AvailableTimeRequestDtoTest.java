package roomescape.service.dto;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AvailableTimeRequestDtoTest {

    private final String validDate = "2024-12-20";
    private final Long validThemeId = 1L;

    @DisplayName("테마 아이디가 입력되지 않으면 예외가 발생한다.")
    @Test
    void throw_exception_when_null_theme_id_input() {
        assertThatThrownBy(() -> new AvailableTimeRequestDto(validDate, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 아이디는 반드시 입력되어야 합니다.");
    }

    @DisplayName("테마 아이디가 자연수가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void throw_exception_when_not_natural_theme_id_input(Long themeId) {
        assertThatThrownBy(() -> new AvailableTimeRequestDto(validDate, themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디는 자연수여야 합니다.");
    }

    @DisplayName("날짜가 입력되지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throw_exception_when_null_date_input(String date) {
        assertThatThrownBy(() -> new AvailableTimeRequestDto(date, validThemeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜는 반드시 입력되어야 합니다.");
    }

    @DisplayName("잘못된 날짜 형식이 입력되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"23", "1-12-20", "2014-11", "2015-13-01", "2016-02-30", "2019-09-31", "2022-05-00"})
    void throw_exception_when_invalid_date_format_input(String date) {
        assertThatThrownBy(() -> new AvailableTimeRequestDto(date, validThemeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜 형식이 올바르지 않습니다.");
    }

    @DisplayName("유효한 예약 입력 시 정상 생성된다.")
    @Test
    void create_success() {
        assertThatNoException()
                .isThrownBy(() -> new AvailableTimeRequestDto(validDate, validThemeId));
    }
}
