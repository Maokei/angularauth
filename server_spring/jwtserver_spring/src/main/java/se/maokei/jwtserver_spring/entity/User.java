package se.maokei.jwtserver_spring.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class User {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
}
