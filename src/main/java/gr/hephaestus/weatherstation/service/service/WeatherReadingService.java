package gr.hephaestus.weatherstation.service.service;
import gr.hephaestus.weatherstation.service.model.WeatherReading;
import gr.hephaestus.weatherstation.service.repository.WeatherReadingRepository;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public ByteArrayOutputStream exportReadingsToExcel(String fromDateStr, String toDateStr) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime from = LocalDate.parse(fromDateStr, formatter).atStartOfDay();
        LocalDateTime to = LocalDate.parse(toDateStr, formatter).atTime(23, 59, 59);

        List<WeatherReading> readings = getReadingsBetween(from, to);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Weather Readings");

        String[] headers = {"ID", "Temperature", "Humidity", "Wind Direction", "Wind Speed", "Is Raining", "Rain Density", "Created"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (WeatherReading reading : readings) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(reading.getId().toString());
            row.createCell(1).setCellValue(reading.getTemperature());
            row.createCell(2).setCellValue(reading.getHumidity());
            row.createCell(3).setCellValue(reading.getWindDirection());
            row.createCell(4).setCellValue(reading.getWindSpeed());
            row.createCell(5).setCellValue(reading.isRaining());
            row.createCell(6).setCellValue(reading.getRainDensity());
            row.createCell(7).setCellValue(reading.getCreated().toString());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out;
    }

}