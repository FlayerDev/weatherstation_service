#This file is AI generated!!

import paho.mqtt.client as mqtt
import random
import json
import time

# MQTT Broker Configuration
BROKER = "localhost"  # Change if using a different broker
PORT = 1883
TOPIC = "weather/update"

# Convert wind degree (0-360) to direction
def get_wind_direction(degrees):
    directions = ["N", "NE", "E", "SE", "S", "SW", "W", "NW"]
    index = round(degrees / 45) % 8
    return directions[index]

# MQTT Connection Callback
def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT Broker!")
    else:
        print(f"Failed to connect, return code {rc}")

# MQTT Client Setup
client = mqtt.Client()
client.on_connect = on_connect
client.connect(BROKER, PORT, 60)

# Main Loop - Sends a message every second
while True:
    # Generate random weather data
    temperature = round(random.uniform(-10, 35), 2)  # -10°C to 35°C
    humidity = round(random.uniform(10, 100), 2)  # 10% to 100%
    wind_speed = round(random.uniform(0, 30), 2)  # 0 to 30 km/h
    wind_degrees = random.uniform(0, 360)  # 0° to 360°
    wind_direction = get_wind_direction(wind_degrees)
    is_raining = random.choice([True, False])
    rain_density = round(random.uniform(0, 10), 2) if is_raining else 0  # 0 to 10 mm/h

    # Create JSON message
    weather_data = {
        "temperature": temperature,
        "humidity": humidity,
        "windSpeed": wind_speed,
        "windDirection": wind_direction,
        #"isRaining": is_raining,
        "rainDensity": rain_density
    }

    # Convert to JSON and publish
    message = json.dumps(weather_data)
    client.publish(TOPIC, message)

    print(f"Published: {message}")

    time.sleep(1)  # Send every second