
#include "mqtt.h"

class MQTT_Manager{
    MQTT_Manager(const char* mqtt_server, const int mqtt_port, const char* mqtt_user, const char* mqtt_password, const char* mqtt_client_id, const char* wifi_ssid, const char* wifi_password) {
        this->mqtt_server = mqtt_server;
        this->mqtt_port = mqtt_port;
        this->mqtt_user = mqtt_user;
        this->mqtt_password = mqtt_password;
        this->mqtt_client_id = mqtt_client_id;
        this->wifi_ssid = wifi_ssid;
        this->wifi_password = wifi_password;
    }
    
    void setup(){
        // Serial console
        Serial.begin(115200);
        Serial.setTimeout(500);

        // WiFi
        setup_wifi();

        // MQTT
        client.setServer(mqtt_server, mqtt_port);
        client.setCallback(callback);

        reconnect();
    }
    
     void publish(const char* topic, const char* payload){
        client.publish(topic, payload);
    }

    void subscribe(const char* topic){
        client.subscribe(topic);
    }

    void callback(char* topic, byte* payload, unsigned int length){
        char* response = "";
        resonse.append("Message arrived [");
        response.append(topic);
        response.append("] ");
        for (int i = 0; i < length; i++) {
            response.append((char)payload[i]);
        }
        this->mqtt_response = response;
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

    void setup_wifi(){
        // Connect to WiFi network
        Serial.println();
        Serial.print("Connecting to ");
        Serial.println(wifi_ssid);

        WiFi.begin(wifi_ssid, wifi_password);

        // Wait for connection
        while (WiFi.status() != WL_CONNECTED) {
            delay(500);
            Serial.print(".");
        }

        // Print local IP address
        Serial.println("");
        Serial.println("WiFi connected");
        Serial.println("IP address: ");
        Serial.println(WiFi.localIP());
    }

    void loop(){
        client.loop();
    }

    PubSubClient getClient(){
        return this->client;
    }

    int getMqttPort(){
        return this->mqtt_port;
    }

    const char* getMqttServer(){
        return this->mqtt_server;
    }

    const char* getMqttUser(){
        return this->mqtt_user;
    }

    const char* getMqttPassword(){
        return this->mqtt_password;
    }

    const char* getMqttClientId(){
        return this->mqtt_client_id;
    }

    const char* getWifiSsid(){
        return this->wifi_ssid;
    }

    const char* getWifiPassword(){
        return this->wifi_password;
    }

    void print(){
        Serial.println("MQTT_Manager {");
        Serial.print("mqtt_server: ");
        Serial.println(this->mqtt_server);
        Serial.print("mqtt_port: ");
        Serial.println(this->mqtt_port);
        Serial.print("mqtt_user: ");
        Serial.println(this->mqtt_user);
        Serial.print("mqtt_password: ");
        Serial.println(this->mqtt_password);
        Serial.print("mqtt_client_id: ");
        Serial.println(this->mqtt_client_id);
        Serial.print("wifi_ssid: ");
        Serial.println(this->wifi_ssid);
        Serial.print("wifi_password: ");
        Serial.println(this->wifi_password);
        Serial.println("}");
    }

    char* getResponse(){
        return this->mqtt_response;
    }
