#include "Sensor_de_luz.ino"

Light_sensor light_sensor(A0, 500);

void setup() {
  Serial.begin(9600);
  light_sensor.setup();
}

void loop() {
    // Check if light_sensor can see
    if (light_sensor.can_see()) {
        // Turn on the bulb
        digitalWrite(light_sensor.getBulbPin(), LOW);
    } else {
        // Turn off the bulb
        digitalWrite(light_sensor.getBulbPin(), HIGH);
    }
}