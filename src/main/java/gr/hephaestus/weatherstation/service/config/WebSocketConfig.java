package gr.hephaestus.weatherstation.service.config;

import gr.hephaestus.weatherstation.service.websocket.WeatherReadingWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WeatherReadingWebSocketHandler weatherReadingWebSocketHandler;

    public WebSocketConfig(WeatherReadingWebSocketHandler weatherReadingWebSocketHandler) {
        this.weatherReadingWebSocketHandler = weatherReadingWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(weatherReadingWebSocketHandler, "/weather/live")
                .setAllowedOrigins("*"); // Allow all origins (adjust if needed)
    }
}