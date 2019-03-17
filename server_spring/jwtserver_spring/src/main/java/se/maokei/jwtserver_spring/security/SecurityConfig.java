package se.maokei.jwtserver_spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import se.maokei.jwtserver_spring.repository.SecurityContextRepository;


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/api/login").permitAll()
                .pathMatchers("/api/register").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/events**").permitAll()
                .pathMatchers(HttpMethod.DELETE, "/api/events/**").hasRole("ROLE_ADMIN")
                .pathMatchers(HttpMethod.GET, "/api/special/**").authenticated()
                .anyExchange().authenticated()
                .and().build();

    }

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }*/
}
