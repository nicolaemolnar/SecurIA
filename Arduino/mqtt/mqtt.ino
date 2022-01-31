# include <WiFi.h>
# include <PubSubClient.h>

# define WIFI_SSID "MOVISTAR_8CC0"
# define WIFI_PASS "7Y6L7o7wUfvB6GAC32Ns"

const char* mqttServer = "192.168.1.38";
const int mqttPort = 5555;

int ldr_pin = 35;
int light_threshold = 150;

int ir_pin[] = {36};

int us_echo = 32;
int us_trigger = 39;
int us_threshold = 10;
                                                                                                                                      
int light_bulb = 33;

long init_time;

WiFiClient espClient;
PubSubClient client(espClient);

void setup(){
    Serial.begin(115200);
    //initWifi();
    //initMQTT();

    setup_light_sensor(ldr_pin);
    setup_ultrasonic_sensor(us_trigger, us_echo);
    for (int i = 0; i < (sizeof(ir_pin)/sizeof(ir_pin)[0]); i++) {
        setup_ir_sensor(ir_pin[i]);
    }
    pinMode(light_bulb, OUTPUT);

    init_time = millis();
}

void loop() {
    
    int delayed_time = millis() - init_time;
    
    // Get proximity sensor value
    if (is_near_entity(us_trigger, us_echo, us_threshold)) {
      Serial.println("Entity is near");
      // Check the light sensor value
        if (can_see_entity(ldr_pin,light_threshold)){
          Serial.println("Day");
          digitalWrite(light_bulb, LOW);
        }else{
          Serial.println("Night");
          digitalWrite(light_bulb, HIGH);
        }
    }else{
      digitalWrite(light_bulb, LOW);
    }

    // Get movement sensors value
    for (int i = 0; i < (sizeof(ir_pin)/sizeof(ir_pin)[0]); i++) {
      delay(500);
        if (detect_movement(ir_pin[i]) && delayed_time%100==0) {
            Serial.printf("Movement detected on sensor %d\n",i);
        }
    }

   
    

    // Send all the values needed to the MQTT broker
    
}

void initWifi(){
    Serial.println("Connecting to WiFi");
    WiFi.begin(WIFI_SSID, WIFI_PASS);
    while (WiFi.status() != WL_CONNECTED){
        Serial.print(".");
        delay(500);
    }

    Serial.println("\nConnected to WiFi");
}

void on_message(){
  
}

void initMQTT(){
    client.setServer(mqttServer, mqttPort);
    reconnect();
}

void reconnect(){
    while (!client.connected()){
        Serial.print("Connecting to MQTT...");
        if (client.connect("ESP32Client")){
            Serial.println("connected");
        } else {
            Serial.print("failed, rc=");
            Serial.print(client.state());
            Serial.println(" trying again in 2 seconds");
            delay(2000);
        }
    }
}

void setup_ultrasonic_sensor(int trigger_pin, int echo_pin) {
    pinMode(trigger_pin, OUTPUT);
    pinMode(echo_pin, INPUT);
}

void setup_ir_sensor(int ir_pin){
    pinMode(ir_pin, INPUT);
}

void setup_light_sensor(int ldr_pin){
    pinMode(ldr_pin, INPUT);
}

bool detect_movement(int ir_pin){
    int val = digitalRead(ir_pin);

    delay(10);
    Serial.printf("%d\n",val);
    int val2 = digitalRead(ir_pin);
    return val != val2;
}

bool can_see_entity(int ldr_pin, int threshold){
    int light = analogRead(ldr_pin);
    //Serial.print("light:");
    //Serial.println(light);
    return light > threshold;
}

int get_distance(int us_trigger, int us_echo){
    digitalWrite(us_trigger, LOW);
    delayMicroseconds(2);
    digitalWrite(us_trigger, HIGH);
    delayMicroseconds(10);
    digitalWrite(us_trigger, LOW);

    int duration = pulseIn(us_echo, HIGH);
    int distance = duration / 29 / 2;
    return distance;
}

bool is_near_entity(int us_trigger, int us_echo, int threshold){
    int distance = get_distance(us_trigger, us_echo);
    //Serial.println(distance);
    return distance <= threshold && distance > 0;
}
