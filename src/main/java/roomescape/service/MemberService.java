package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberPassword;
import roomescape.global.JwtManager;
import roomescape.repository.JdbcMemberRepository;
import roomescape.service.dto.member.LoginMemberRequest;
import roomescape.service.dto.member.MemberCreateRequest;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.exception.MemberNotFoundException;

@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;
    private final JwtManager jwtManager;

    public MemberService(JdbcMemberRepository memberRepository, JwtManager jwtManager) {
        this.memberRepository = memberRepository;
        this.jwtManager = jwtManager;
    }

    public void signup(MemberCreateRequest request) {
        if (memberRepository.isMemberExistsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입되어 있는 이메일 주소입니다.");
        }
        memberRepository.insertMember(request.toMember());
    }

    public String login(LoginMemberRequest request) {
        Member member = memberRepository.findMemberByEmail(request.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("회원 정보가 존재하지 않습니다."));

        MemberPassword requestPassword = new MemberPassword(request.getPassword());
        if (member.isMismatchedPassword(requestPassword)) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return jwtManager.generateToken(member);
    }

    public List<MemberResponse> findAllMemberNames() {
        return memberRepository.findAllMemberNames().stream()
                .map(MemberResponse::new)
                .toList();
    }
}
