// This file is AI generated!!!

package gr.hephaestus.weatherstation.service.service;

import gr.hephaestus.weatherstation.service.model.WeatherReading;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Service
public class WeatherUpdateService {

    private final WeatherReadingService weatherReadingService;
    private static final String MQTT_BROKER = "tcp://localhost:1883";
    private static final String MQTT_TOPIC = "weather/update";

    public WeatherUpdateService(WeatherReadingService weatherReadingService) {
        this.weatherReadingService = weatherReadingService;
    }

    @EventListener(ApplicationReadyEvent.class) // Runs after Spring Boot starts
    public void startMqttClient() {
        try {
            MqttClient client = new MqttClient(MQTT_BROKER, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);

            client.subscribe(MQTT_TOPIC, this::handleMqttMessage);
            System.out.println("Subscribed to MQTT topic: " + MQTT_TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMqttMessage(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());
            System.out.println("Received MQTT: " + payload);

            // Convert JSON to WeatherReading
            ObjectMapper objectMapper = new ObjectMapper();
            WeatherReading reading = objectMapper.readValue(payload, WeatherReading.class);

            // Save to database
            weatherReadingService.addReading(
                    reading.getTemperature(),
                    reading.getHumidity(),
                    reading.getWindDirection(),
                    reading.getWindSpeed(),
                    reading.isRaining(),
                    reading.getRainDensity()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}