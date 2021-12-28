#include "light_sensor.h"

 Light_sensor(int analogPin, int minLight){
    this->analogPin = analogPin;
    this->minLight = minLight;
    this->lightvalue = 0;
  }

  void setup(){
    pinMode(analogPin, INPUT);
  }

  int read(){
    lightvalue = analogRead(analogPin);
  }

  void setAnalogPin(int pin){
    this->analogPin = pin;
  }

  void setMinLight(int min){
    this->minLight = min;
  }

  int getLightValue(){
    return lightvalue;
  }

  int getMinLight(){
    return minLight;
  }

  bool can_see(){
    return lightvalue > minLight;
  }
