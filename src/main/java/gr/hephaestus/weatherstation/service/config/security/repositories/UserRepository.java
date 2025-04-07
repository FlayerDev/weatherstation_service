package gr.hephaestus.weatherstation.service.config.security.repositories;

/*
 *
 *  @Code from GitHub repository :  https://github.com/xartokoptiko/spring-jwt-conf
 *
 */

import gr.hephaestus.weatherstation.service.config.security.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
