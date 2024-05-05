package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.repository.DatabaseCleanupListener;
import roomescape.service.dto.AvailableTimeResponseDto;
import roomescape.service.dto.AvailableTimeResponseDtos;
import roomescape.service.dto.ReservationTimeResponseDto;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationTimeApiControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private final Map<String, String> reservationTimeCreate1 = Map.of(
            "startAt", "10:00"
    );

    private final Map<String, String> reservationTimeCreate2 = Map.of(
            "startAt", "12:00"
    );

    private final Map<String, Object> reservationCreate1 = Map.of(
            "name", "재즈",
            "date", "2100-08-05",
            "timeId", 1,
            "themeId", 1
    );

    private final Map<String, String> themeCreate1 = Map.of(
            "name", "공포",
            "description", "공포는 무서워",
            "thumbnail", "hi.jpg"
    );

    @DisplayName("예약 시간을 생성하는데 성공하면 응답과 201 상태 코드를 반환한다.")
    @Test
    void return_201_when_create_reservation_time() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeCreate1)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("예약 시간 목록을 조회하는데 성공하면 응답과 200 상태 코드를 반환한다.")
    @Test
    void return_200_when_find_all_reservation_times() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeCreate1)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        List<ReservationTimeResponseDto> actualResponse = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationTimeResponseDto.class);

        ReservationTimeResponseDto expectedResponse = new ReservationTimeResponseDto(1L, "10:00");

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .isEqualTo(List.of(expectedResponse));
    }

    @DisplayName("예약 가능한 시간을 조회하는데 성공하면 응답과 200 상태 코드를 반환한다.")
    @Test
    void return_200_when_find_available_reservation_times() {
        create("/themes", themeCreate1);
        create("/times", reservationTimeCreate1);
        create("/times", reservationTimeCreate2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationCreate1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        AvailableTimeResponseDtos actualResponse = RestAssured.given().log().all()
                .when().get("/times/available?date=2100-08-05&theme-id=1")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject(".", AvailableTimeResponseDtos.class);

        AvailableTimeResponseDto availableTimeResponseDto1 = new AvailableTimeResponseDto(
                new ReservationTimeResponseDto(1L, "10:00"), true);
        AvailableTimeResponseDto availableTimeResponseDto2 = new AvailableTimeResponseDto(
                new ReservationTimeResponseDto(2L, "12:00"), false);
        AvailableTimeResponseDtos expectedResponse = new AvailableTimeResponseDtos(List.of(
                availableTimeResponseDto1, availableTimeResponseDto2
        ));

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    private void create(String path, Map<String, String> param) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post(path)
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("예약 시간을 삭제하는데 성공하면 응답과 204 상태 코드를 반환한다.")
    @Test
    void return_204_when_delete_reservation_time() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeCreate1)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }
}
