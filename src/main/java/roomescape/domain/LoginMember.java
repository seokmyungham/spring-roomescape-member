package roomescape.domain;

public class LoginMember {

    private final Long id;
    private final String email;
    private final String name;
    private final Role role;

    public LoginMember(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
