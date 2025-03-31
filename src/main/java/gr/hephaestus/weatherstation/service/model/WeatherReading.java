package gr.hephaestus.weatherstation.service.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class WeatherReading {

    @Id
    @GeneratedValue
    private UUID id;

    private double temperature;
    private double humidity;
    private String windDirection;
    private double windSpeed;
    private boolean isRaining;
    private double rainDensity;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    // Constructors
    public WeatherReading() {
    }

    public WeatherReading(double temperature, double humidity, String windDirection, double windSpeed, boolean isRaining, double rainDensity) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.isRaining = isRaining;
        this.rainDensity = rainDensity;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public boolean isRaining() {
        return isRaining;
    }

    public double getRainDensity() {
        return rainDensity;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public boolean isValidReading() {
        return temperature >= -50 && temperature <= 60 && humidity >= 0 && humidity <= 100;
    }

    @Override
    public String toString() {
        return "WeatherReading{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", windDirection='" + windDirection + '\'' +
                ", windSpeed=" + windSpeed +
                ", isRaining=" + isRaining +
                ", rainDensity=" + rainDensity +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}