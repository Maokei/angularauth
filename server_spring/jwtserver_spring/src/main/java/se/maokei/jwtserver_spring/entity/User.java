package se.maokei.jwtserver_spring.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
    @Id
    private String id;
    @Email
    @NotNull
    @Indexed(unique = true)
    private String email;
    @NotNull @Getter @Setter
    private String password;
    @Getter @Setter
    private Boolean enabled;
    @Getter @Setter
    private List<Role> roles;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
