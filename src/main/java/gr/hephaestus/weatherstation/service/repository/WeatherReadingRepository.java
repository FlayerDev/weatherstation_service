package gr.hephaestus.weatherstation.service.repository;

import gr.hephaestus.weatherstation.service.model.WeatherReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WeatherReadingRepository extends JpaRepository<WeatherReading, UUID> {

    // Get the latest weather reading
    Optional<WeatherReading> findTopByOrderByCreatedDesc();

    // Get all weather readings between two timestamps
    List<WeatherReading> findByCreatedBetween(LocalDateTime start, LocalDateTime end);
}
