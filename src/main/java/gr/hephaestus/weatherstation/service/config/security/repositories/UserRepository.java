package gr.hephaestus.weatherstation.service.config.security.repositories;


import gr.hephaestus.weatherstation.service.config.security.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
