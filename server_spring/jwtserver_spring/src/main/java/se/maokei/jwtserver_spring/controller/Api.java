package se.maokei.jwtserver_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.maokei.jwtserver_spring.entity.Event;
import se.maokei.jwtserver_spring.entity.User;
import se.maokei.jwtserver_spring.repository.UserRepository;
import se.maokei.jwtserver_spring.security.AuthRequest;
import se.maokei.jwtserver_spring.security.AuthResponse;
import se.maokei.jwtserver_spring.security.JWTUtil;

import java.util.*;

@RestController
@RequestMapping(value = "/api" )
public class Api {
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> helloFromApi() {
        return Collections.singletonMap("response","Hello from api");
    }

    @GetMapping("/events")
    public Flux<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Cooking Meetup","Bring carrots", new Date()));
        events.add(new Event("Auto export","Data export party",new Date()));
        events.add(new Event("Exorcism","Exercise a demon seminar",new Date()));
        events.add(new Event("Snow sculpting","Sculpt snow statues",new Date()));
        return Flux.fromIterable(events);
    }

    @GetMapping("/special")
    public Flux<Event> getSpecial() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Cooking Meetup","Bring carrots", new Date()));
        events.add(new Event("Auto export","Data export party",new Date()));
        events.add(new Event("Exorcism","Exercise a demon seminar",new Date()));
        events.add(new Event("Snow sculpting","Sculpt snow statues",new Date()));
        return Flux.fromIterable(events);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        Optional<User> oUser = Optional.ofNullable(this.userRepository.findUserByEmail(email));
        if(oUser.isPresent()) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("response","Bad user data")));
        }else{
            ArrayList<User.Role> roles  = new ArrayList<>();
            roles.add(User.Role.ROLE_USER);
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setEmail(email);
            newUser.setRoles(roles);
            this.userRepository.save(newUser);
            return Mono.just(ResponseEntity.ok(Collections.singletonMap("response","User registered")));
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        User ud = this.userRepository.findUserByEmail(ar.getUsername());
        if (passwordEncoder.matches(ar.getPassword(), ud.getPassword())) {
            return Mono.just(ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(ud))));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }
}
