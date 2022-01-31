#ifndef PROXIMITY_SENSOR_H
#define PROXIMITY_SENSOR_H

#include "Arduino.h"

class Proximity_sensor{
  private:
    int trigger_pin;
    int echo_pin;
    int threshold;
  public:
    ProximitySensor(int trigger_pin, int echo_pin, int threshold);
    void setup();
    int getDistance();
    bool entity_is_near();
    int getTriggerPin();
    int getEchoPin();
    int getThreshold();
    void setThreshold(int threshold);
    void setTriggerPin(int trigger_pin);
    void setEchoPin(int echo_pin);
    void print();
}
