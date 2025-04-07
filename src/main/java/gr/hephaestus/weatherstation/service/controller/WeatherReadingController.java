package gr.hephaestus.weatherstation.service.controller;

import gr.hephaestus.weatherstation.service.model.WeatherReading;
import gr.hephaestus.weatherstation.service.service.WeatherReadingService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<?> getReadingsBetween(
            @RequestParam("from") String fromDateStr,
            @RequestParam("to") String toDateStr,
            @RequestParam("export_type") String exportType
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime from = LocalDate.parse(fromDateStr, formatter).atStartOfDay();
        LocalDateTime to = LocalDate.parse(toDateStr, formatter).atTime(23, 59, 59);

        if (exportType.equalsIgnoreCase("raw")) {
            List<WeatherReading> readings = weatherReadingService.getReadingsBetween(from, to);
            return ResponseEntity.ok(readings);
        } else if (exportType.equalsIgnoreCase("excel")) {
            try {
                ByteArrayOutputStream excelStream = weatherReadingService.exportReadingsToExcel(fromDateStr, toDateStr);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=weather_readings.xlsx")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(excelStream.toByteArray());

            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error generating Excel file: " + e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid export_type. Use 'raw' or 'excel'.");
        }
    }
}