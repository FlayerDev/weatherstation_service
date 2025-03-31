package gr.hephaestus.weatherstation.service.controller;

import gr.hephaestus.weatherstation.service.model.WeatherReading;
import gr.hephaestus.weatherstation.service.service.WeatherReadingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherReadingController {

    private final WeatherReadingService weatherReadingService;

    public WeatherReadingController(WeatherReadingService weatherReadingService) {
        this.weatherReadingService = weatherReadingService;
    }

    // GET /weather/last → Returns the latest weather reading
    @GetMapping()
    public Optional<WeatherReading> getLastReading() {
        return weatherReadingService.getLastReading();
    }

    // GET /weather?from=YYYY-MM-DDTHH:MM&to=YYYY-MM-DDTHH:MM → Returns readings within a range
    @GetMapping("/history")
    public List<WeatherReading> getReadingsBetween(
            @RequestParam("from") LocalDateTime from,
            @RequestParam("to") LocalDateTime to
    ) {
        return weatherReadingService.getReadingsBetween(from, to);
    }
}