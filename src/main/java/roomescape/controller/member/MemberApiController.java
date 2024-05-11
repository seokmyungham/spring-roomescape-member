package roomescape.controller.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.resolver.AuthenticationPrincipal;
import roomescape.service.MemberService;
import roomescape.service.dto.member.CreateMemberRequest;
import roomescape.service.dto.member.LoginMember;
import roomescape.service.dto.member.LoginMemberRequest;
import roomescape.service.dto.member.MemberResponse;

@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/members/signup")
    public void signup(@Valid @RequestBody CreateMemberRequest request) {
        memberService.signup(request);
    }

    @PostMapping("/members/login")
    public void login(@Valid @RequestBody LoginMemberRequest request, HttpServletResponse response) {
        String token = memberService.login(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(300);
        response.addCookie(cookie);
    }

    @GetMapping("/members/login/check")
    public MemberResponse checkLogin(@AuthenticationPrincipal LoginMember loginMember) {
        return new MemberResponse(loginMember);
    }

    @PostMapping("/members/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/admin/members")
    public List<MemberResponse> findAllMembers() {
        return memberService.findAllMemberNames();
    }
}
