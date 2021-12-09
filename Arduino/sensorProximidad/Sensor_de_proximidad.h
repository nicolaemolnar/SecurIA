#define ProximitySensor
class ProximitySensor{
    private:
        int trigger_pin;
        int echo_pin;
        int distance;
        int duration;
        int threshold;

    public:
        ProximitySensor(int trigger_pin, int echo_pin, int threshold);
        void setup();
        int getDistance();
        int getTriggerPin();
        int getEchoPin();
        int getThreshold();
        void setThreshold(int threshold);
        void setTriggerPin(int trigger_pin);
        void setEchoPin(int echo_pin);
        bool entity_is_near();
}