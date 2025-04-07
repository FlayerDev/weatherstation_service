package gr.hephaestus.weatherstation.service.config.security.repositories;

import gr.hephaestus.weatherstation.service.config.security.entities.VerificationToken;
import jakarta.annotation.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
