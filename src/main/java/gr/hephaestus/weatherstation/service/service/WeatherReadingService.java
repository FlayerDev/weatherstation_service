package gr.hephaestus.weatherstation.service.service;
import gr.hephaestus.weatherstation.service.model.WeatherReading;
import gr.hephaestus.weatherstation.service.repository.WeatherReadingRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherReadingService {

    private final WeatherReadingRepository weatherReadingRepository;

    public WeatherReadingService(WeatherReadingRepository weatherReadingRepository) {
        this.weatherReadingRepository = weatherReadingRepository;
    }

    // Add a new weather reading
    public WeatherReading addReading(double temperature, double humidity, String windDirection,
                                     double windSpeed, boolean isRaining, double rainDensity) {
        WeatherReading reading = new WeatherReading(temperature, humidity, windDirection, windSpeed, isRaining, rainDensity);
        return weatherReadingRepository.save(reading);
    }

    // Get the latest weather reading
    public Optional<WeatherReading> getLastReading() {
        return weatherReadingRepository.findTopByOrderByCreatedDesc();
    }

    // Get all readings between two times
    public List<WeatherReading> getReadingsBetween(LocalDateTime start, LocalDateTime end) {
        return weatherReadingRepository.findByCreatedBetween(start, end);
    }

    // Get all readings
    public List<WeatherReading> getAllReadings() {
        return weatherReadingRepository.findAll();
    }
}