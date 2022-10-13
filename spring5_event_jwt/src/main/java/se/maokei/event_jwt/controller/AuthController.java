package se.maokei.event_jwt.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.maokei.event_jwt.dto.LoginDto;
import se.maokei.event_jwt.dto.JWTTokenDto;
import se.maokei.event_jwt.security.JwtFilter;
import se.maokei.event_jwt.security.JwtTokenProvider;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDto> login(@RequestBody LoginDto authDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authDTO.getUsername(),
                authDTO.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        final String jwt = tokenProvider.createToken(authentication, authDTO.isRememberMe());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new JWTTokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
