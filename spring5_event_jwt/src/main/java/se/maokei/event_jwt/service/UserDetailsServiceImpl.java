package se.maokei.event_jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.maokei.event_jwt.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Map<String, User> users = new HashMap<>();

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        final User user = new User();
        user.setId(1);
        user.setEmail("user@gmail.com");
        user.setRoles(List.of(User.Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode("password")); //password

        final User user2 = new User();
        user2.setId(2);
        user2.setEmail("admin@gmail.com");
        user2.setRoles(List.of(User.Role.ROLE_USER, User.Role.ROLE_ADMIN));
        user2.setPassword(passwordEncoder.encode("password")); //password

        users.put(user.getEmail(), user);
        users.put(user2.getEmail(), user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User found = users.get(username);

        if (found == null) {
            throw new UsernameNotFoundException("Username not found!");
        }

        return found;
    }
}
