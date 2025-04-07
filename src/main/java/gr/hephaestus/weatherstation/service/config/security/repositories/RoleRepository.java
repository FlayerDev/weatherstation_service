package gr.hephaestus.weatherstation.service.config.security.repositories;

/*
 *
 *  @Code from GitHub repository :  https://github.com/xartokoptiko/spring-jwt-conf
 *
 */

import gr.hephaestus.weatherstation.service.config.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
