#include "proximity_sensor.h"

  ProximitySensor(int trigger_pin, int echo_pin, int threshold) {
    this->trigger_pin = trigger_pin;
    this->echo_pin = echo_pin;
    this->threshold = threshold;
    this->distance = 0;
    this->duration = 0;
  }

  void setup(){
    pinMode(this->trigger_pin, OUTPUT);
    pinMode(this->echo_pin, INPUT);
  }

  int getDistance(){
    digitalWrite(this->trigger_pin, LOW);
    delayMicroseconds(2);
    digitalWrite(this->trigger_pin, HIGH);
    delayMicroseconds(10);
    digitalWrite(this->trigger_pin, LOW);

    duration = pulseIn(this->echo_pin, HIGH);
    distance = duration / 29 / 2;
    return distance;
  }

  bool entity_is_near(){
    return getDistance() <= threshold;
  }

  int getTriggerPin(){
    return this->trigger_pin;
  }

  int getEchoPin(){
    return this->echo_pin;
  }

  int getThreshold(){
    return this->threshold;
  }

  void setThreshold(int threshold){
    this->threshold = threshold;
  }

  void setTriggerPin(int trigger_pin){
    this->trigger_pin = trigger_pin;
  }

  void setEchoPin(int echo_pin){
    this->echo_pin = echo_pin;
  }

  void print(){
    Serial.println("Proximity Sensor {")
    Serial.print("  Trigger pin: ");
    Serial.println(trigger_pin);
    Serial.print("  Echo pin: ");
    Serial.println(echo_pin);
    Serial.print("  Distance: ");
    Serial.println(getDistance());
    Serial.print("  Threshold: ");
    Serial.println(threshold);
    Serial.println("}");
  }
