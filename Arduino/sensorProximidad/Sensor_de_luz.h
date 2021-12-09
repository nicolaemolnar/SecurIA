#ifndef Sensor_de_luz_h
#define Sensor_de_luz_h
class Sensor_de_luz{
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
