package roomescape.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import roomescape.service.dto.validator.DateFormatConstraint;

public class PopularThemeRequestDto {

    @DateFormatConstraint
    private final String startDate;

    @DateFormatConstraint
    private final String endDate;

    @NotNull(message = "불러올 테마 개수는 반드시 입력되어야 합니다.")
    @Positive(message = "불러올 테마 개수는 자연수여야 합니다. 입력 값: ${validatedValue}은 사용할 수 없습니다.")
    private final Integer count;

    public PopularThemeRequestDto(String startDate, String endDate, Integer count) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.count = count;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getCount() {
        return count;
    }
}
