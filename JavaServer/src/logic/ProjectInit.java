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
        /*Log.log.info("Initializing the server...");
        MQTTBroker mqttBroker = new MQTTBroker("SecurIA Central Broker", "25.62.36.206", 5555, 2);
        Log.log.info("MQTT Broker created");
        MQTTSubscriber mqttSub = new MQTTSubscriber();
        Log.log.info("MQTT Subscriber created");
        MQTTPublisher mqttPub = new MQTTPublisher();
        Log.log.info("MQTT Publisher created");*/
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Log.log.info("Server service stopped.");
    }
}
