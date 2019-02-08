package se.maokei.jwtserver_spring.repository;

import org.springframework.data.repository.CrudRepository;
import se.maokei.jwtserver_spring.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail(String email);
}
