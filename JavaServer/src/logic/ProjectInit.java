package logic;

import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSubscriber;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ProjectInit implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // init the project
        Log.log.info("Initializing the server...");
        MQTTBroker mqttBroker = new MQTTBroker(2, "localhost", 1883, "SecurIA Central Broker");
        Log.log.info("MQTT Broker created");
        MQTTSubscriber mqttSub = new MQTTSubscriber();
        Log.log.info("MQTT Subscriber created");
        MQTTPublisher mqttPub = new MQTTPublisher();
        Log.log.info("MQTT Publisher created");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Log.log.info("Server service stopped.");
    }
}
