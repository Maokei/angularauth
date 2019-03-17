package se.maokei.jwtserver_spring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SecurityUtil {
    @Value("${server.password.encoder.secret}")
    private String secret;

    @Value("${server.password.encoder.strength}")
    private Integer strength;

    @Bean
    public BCryptPasswordEncoder passwordEncoder()  {
        return new BCryptPasswordEncoder(strength, new SecureRandom(secret.getBytes()));
    }
}
