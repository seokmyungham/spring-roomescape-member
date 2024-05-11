package roomescape.service.dto.reservation;

import java.time.LocalTime;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.service.dto.member.LoginMember;
import roomescape.service.dto.member.MemberReservationRequest;

public class CreateReservation {

    private final long memberId;
    private final long themeId;
    private final String date;
    private final long timeId;

    public CreateReservation(long memberId, long themeId, String date, long timeId) {
        this.memberId = memberId;
        this.themeId = themeId;
        this.date = date;
        this.timeId = timeId;
    }

    public CreateReservation(LoginMember loginMember, MemberReservationRequest requestDto) {
        this(loginMember.getId(), requestDto.getThemeId(), requestDto.getDate(), requestDto.getTimeId());
    }

    public Reservation toReservation() {
        return new Reservation(
                null,
                new Member(memberId, null, null, null),
                new Theme(themeId, (String) null, null, null),
                new ReservationDate(date),
                new ReservationTime(timeId, (LocalTime) null)
        );
    }
}
