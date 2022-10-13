package se.maokei.event_jwt.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginDto {
    @NotNull
    @NotBlank
    private String username;
    private String password;
    private boolean rememberMe;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "username='" + username + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }
}
