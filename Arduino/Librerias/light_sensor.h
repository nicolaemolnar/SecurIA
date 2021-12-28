#ifndef LIGHT_SENSOR_H
#define LIGHT_SENSOR_H

#include <Arduino.h>
class Light_sensor
{
  private:
    int analogPin;
    int lightvalue;
    int minLight;

  public:
    Light_sensor(int analogPin, int minLight, int bulbPin);
    void setup();
    int read();
    void setAnalogPin(int pin);
    void setMinLight(int min);
    int getLightValue();
    int getMinLight();
    bool can_see();
}
#endif
