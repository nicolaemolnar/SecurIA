#include <WiFi.h>
#include <PubSubClient.h>

const char* ssid = "eduroam";
const char* password = "1234";
const char* mqtt_server = "172.0.0.1";
const int mqtt_port = 5555;
const char* mqtt_user = "esp32";
const char* mqtt_password = "1234";

WiFiClient esp_client;
PubSubClient client(esp_client);

void setup_wifi(){
    delay(10);
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);

    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    
    randomSeed(micros());

    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
}

void reconnect(){
    // Loop until reconnected
    while (!client.connected()) {
        Serial.print("Attempting MQTT connection...");
        // Attempt to connect
        if (client.connect("ESP32Client")) {
            Serial.println("connected");
            // Once connected, publish an announcement...
            client.publish("outTopic", "hello world");
            // ... and resubscribe
            client.subscribe("inTopic");
        } else {
            Serial.print("failed, rc=");
            Serial.print(client.state());
            Serial.println(" try again in 5 seconds");
            // Wait 5 seconds before retrying
            delay(5000);
        }
    }
}

void callback(char* topic, byte* payload, unsigned int length) {
    Serial.print("Message arrived [");
    Serial.print(topic);
    Serial.print("] ");
    for (int i = 0; i < length; i++) {
        Serial.print((char)payload[i]);
    }
    Serial.println();
}

void setup(){
    Serial.begin(115200);
    Serial.setTimeout(500);

    setup_wifi();
    client.setServer(mqtt_server, mqtt_port);
    client.setCallback(callback);
    reconnect();
}

void loop(){
    client.loop();

    if (!client.connected()) {
        reconnect();
    }

    if (Serial.available() > 0) {
        String msg = Serial.readString();
        client.publish("outTopic", msg.c_str());
        Serial.println(" --- Message sent to broker --- ");
    }

    // Recieve message from broker
    client.subscribe("inTopic");
}
