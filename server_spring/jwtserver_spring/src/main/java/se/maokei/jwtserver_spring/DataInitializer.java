package se.maokei.jwtserver_spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import se.maokei.jwtserver_spring.entity.User;
import se.maokei.jwtserver_spring.repository.UserRepository;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner{
    @Autowired
    private final UserRepository users;
    @Autowired
    private final BCryptPasswordEncoder enc;

    DataInitializer(UserRepository users, BCryptPasswordEncoder enc) {
        this.users = users;
        this.enc = enc;
    }

    /*@EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        initUsers();
    }*/

    private void initUsers() {
        users.deleteAll();
        System.out.println("Inserting test user");
        this.users.deleteAll();
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(enc.encode("test123"));
        ArrayList roles = new ArrayList();
        roles.add(User.Role.ROLE_ADMIN);
        roles.add(User.Role.ROLE_USER);
        user.setRoles(roles);
        this.users.save(user);
    }

    @Override
    public void run(String... args) throws Exception {
        initUsers();
    }
}
