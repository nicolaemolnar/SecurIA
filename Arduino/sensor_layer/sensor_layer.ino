# include <WiFi.h>
# include <PubSubClient.h>

# define WIFI_SSID "MIWIFI"
# define WIFI_PASS "MICONTRASEÑA"
# define SYSTEM_ID "1"

const char* mqttServer = "192.168.1.38";
const int mqttPort = 5555;
const int n_ir_sensors = 1;

int ldr_pin = 35;
int light_threshold = 150;

int ir_pin[] = {39};
int ir_length;

int us_echo = 34;
int us_trigger = 32;
int us_threshold = 30;
                                                                                                                                      
int light_bulb = 33;

long init_time;
int reset_time = 5000;

int entity_detected = 0;
int prev_us = 0;

int movement_detected = 0;
int prev_ir[n_ir_sensors];

char presence_topic[40];
char presence_message[5];

char movement_topic[40];
char movement_message[5];

WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
    Serial.begin(115200);
    init_wifi();
    init_mqtt();
    setup_light_sensor(ldr_pin);
    setup_ultrasonic_sensor(us_trigger, us_echo);
    for (int i = 0; i < (n_ir_sensors); i++) {
        setup_ir_sensor(ir_pin[i], i);
    }
    pinMode(light_bulb, OUTPUT);

    init_time = millis();
}

void loop() {
    reconnect();
    client.loop();
    long delayed_time = millis() - init_time;
    //Serial.println(delayed_time);
    
    // Get proximity sensor value
    if ((entity_detected = is_near_entity(us_trigger, us_echo, us_threshold))) {
      //Serial.println("Entity is near");

      if (prev_us == 0){
        // Send all the values needed to the MQTT broker
        Serial.println("Sending presence alert to MQTT...");
        sprintf(presence_message, "%d", entity_detected);
        client.publish(presence_topic, presence_message);
        prev_us = entity_detected;
      }
      
      // Check the light sensor value
        if (can_see_entity(ldr_pin,light_threshold)){
          //Serial.println("Day");
          digitalWrite(light_bulb, LOW);
        }else{
          //Serial.println("Night");
          digitalWrite(light_bulb, HIGH);
        }
    }else{
      digitalWrite(light_bulb, LOW);
    }

    // Get movement sensors value
    for (int i = 0; i < n_ir_sensors; i++) {
        if (detect_movement(ir_pin[i]) && prev_ir[i] == 0) {
            Serial.printf("Sending movement alert on sensor %d to MQTT...\n",i+1);
            sprintf(movement_topic, "/sensor/movement/%s/%d", SYSTEM_ID, i+1);
            sprintf(movement_message, "%d", i+1);
            client.publish(movement_topic, movement_message);
            prev_ir[i] = 1;
        }
    }

    if (delayed_time > reset_time){
      prev_us = 0;
      for (int i = 0; i < n_ir_sensors; i++){
        prev_ir[i] = 0;
      }
      init_time = millis();
    }
}

void init_wifi(){
  // Connect to wifi network
  Serial.println("Connecting to WiFi");
    WiFi.begin(WIFI_SSID, WIFI_PASS);
    while (WiFi.status() != WL_CONNECTED){
        Serial.print(".");
        delay(500);
    }

    Serial.println("\nConnected to WiFi");
}

void init_mqtt(){
    client.setServer(mqttServer, mqttPort);
    client.setCallback(on_message);
    reconnect();
}

void on_message(char* topic, byte* payload, unsigned int lenght){
    //Serial.printf("Message received on topic %s: %s\n", topic, (char*) payload);
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
    // Setup presence topic
    sprintf(presence_topic, "/sensor/proximity/%s/", SYSTEM_ID);
    
}

void setup_ir_sensor(int ir_pin, int sensor_id){
    pinMode(ir_pin, INPUT);
}

void setup_light_sensor(int ldr_pin){
    pinMode(ldr_pin, INPUT);
}

bool detect_movement(int ir_pin){
    int val = digitalRead(ir_pin);
    //Serial.println(val);
    return val;
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
