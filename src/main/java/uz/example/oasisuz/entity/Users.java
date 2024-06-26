package uz.example.oasisuz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.example.oasisuz.entity.enums.AuthType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String password;

    private String email;

    private Integer noAdds;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @ManyToMany(mappedBy = "users")
    private List<ChatRoom> chatRoom;

    @OneToMany(mappedBy = "users")
    private List<Cottage> cottageList;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles;

    private String imgUrl;
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach((role) -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum())));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
