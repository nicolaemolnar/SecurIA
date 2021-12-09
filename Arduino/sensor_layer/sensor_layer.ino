
int ldr_pin = A7;
int threshold = 500;

int light_bulb = 13;

void setup() {
    Serial.begin(9600);
    setup_light_sensor(ldr_pin);
    pinMode(light_bulb, OUTPUT);
}

void loop() {
    // TODO: Comunicate with MQTT Broker


    Serial.println(can_see_entity(ldr_pin,threshold));
    // Check if light_sensor can see, if it doesn't, turn on the light_bulb
    if (can_see_entity(ldr_pin, threshold)){
        digitalWrite(light_bulb, LOW);
    }else{
        digitalWrite(light_bulb, HIGH);
    }

}


void setup_light_sensor(int ldr_pin){
    pinMode(ldr_pin, INPUT);
}

bool can_see_entity(int ldr_pin, int threshold){
    int light = analogRead(ldr_pin);
    return light > threshold;
}
