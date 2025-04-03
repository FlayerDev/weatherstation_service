package gr.hephaestus.weatherstation.service.websocket;

import gr.hephaestus.weatherstation.service.service.WeatherReadingService;
import gr.hephaestus.weatherstation.service.model.WeatherReading;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WeatherReadingWebSocketHandler extends TextWebSocketHandler {

    private final WeatherReadingService weatherReadingService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    public WeatherReadingWebSocketHandler(WeatherReadingService weatherReadingService) {
        this.weatherReadingService = weatherReadingService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 1000) // Send update every second
    public void sendLiveWeatherUpdates() {
        if (!sessions.isEmpty()) {
            Optional<WeatherReading> latestReading = weatherReadingService.getLastReading();
            try {
                String json = objectMapper.writeValueAsString(latestReading);
                for (WebSocketSession session : sessions) {
                    session.sendMessage(new TextMessage(json));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}