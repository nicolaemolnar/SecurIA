
int ldr_pin = 33;
int threshold = 150;

int ir_pin = 35;

int light_bulb = 13;

void setup() {
    Serial.begin(9600);
    setup_light_sensor(ldr_pin);
    setup_ir_sensor(ir_pin);
    pinMode(light_bulb, OUTPUT);
}

void loop() {
    // TODO: Comunicate with MQTT Broker
    //Define parameters of the MQTT manager and set up connection
    MQTT_Manager mqtt_manager = MQTT_Manager(const char* mqtt_server, const int mqtt_port, const char* mqtt_user, const char* mqtt_password, const char* mqtt_client_id, const char* wifi_ssid, const char* wifi_password);
    mqtt_manager.setup();
    // Check if light_sensor can see, if it doesn't, turn on the light_bulb
    if (can_see_entity(ldr_pin, threshold)){
        digitalWrite(light_bulb, LOW);
    }else{
        digitalWrite(light_bulb, HIGH);
    }

    if (detect_movement(ir_pin)){
      digitalWrite(light_bulb, HIGH);
    }else{
      digitalWrite(light_bulb, LOW);
    }
}

void setup_ir_sensor(int ir_pin){
    pinMode(ir_pin, INPUT);
}

bool detect_movement(int ir_pin){
    int val = digitalRead(ir_pin);
    Serial.println(val);
    return val == HIGH;
}

void setup_light_sensor(int ldr_pin){
    pinMode(ldr_pin, INPUT);
}

bool can_see_entity(int ldr_pin, int threshold){
    int light = analogRead(ldr_pin);
    //Serial.println(analogRead(ldr_pin));
    return light > threshold;
}
