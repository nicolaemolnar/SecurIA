#ifndef MQTT_H
#define MQTT_H

#include <WiFi.h>
#include <PubSubClient.h>
#include <Arduino.h>


class MQTT_Manager{
  private:
        WiFiClient espClient;
        PubSubClient client = client(espClient);
        const char* mqtt_server;
        const int mqtt_port;
        const char* mqtt_user;
        const char* mqtt_password;
        const char* mqtt_client_id;
        const char* wifi_ssid;
        const char* wifi_password;
        const char* mqtt_response;
        void setup_wifi();
        void callback(char* topic, byte* payload, unsigned int length);
    
    public:
        MQTT_Manager(const char* mqtt_server, const int mqtt_port, const char* mqtt_user, const char* mqtt_password, const char* mqtt_client_id, const char* wifi_ssid, const char* wifi_password);
        void setup();
        void publish(const char* topic, const char* payload);
        void subscribe(const char* topic);
        void loop();
        void reconnect();
        PubSubClient getClient();
        int getMqttPort();
        const char* getMqttServer();
        const char* getMqttUser();
        const char* getMqttPassword();
        const char* getMqttClientId();
        const char* getWifiSsid();
        const char* getWifiPassword();
        void print();
        char* getResponse();
}
#endif
