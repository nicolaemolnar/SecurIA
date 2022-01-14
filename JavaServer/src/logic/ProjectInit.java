package logic;

import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSubscriber;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;

@WebListener
public class ProjectInit implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // init the project
        Log.log.info("Initializing the server...");
        Logic.streams = new HashMap<>();
        Logic.mqttBroker = new MQTTBroker("SecurIA Central Broker", "localhost", 5555, 2);
        Log.logmqtt.info("MQTT Broker created");
        Logic.mqttSubscriber = new MQTTSubscriber(Logic.mqttBroker);
        Log.logmqtt.info("MQTT Subscriber created");
        Logic.mqttSubscriber.searchTopicsToSubscribe();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Logic.mqttSubscriber.disconnectFromBroker();
        Log.log.info("Server service stopped.");
    }
}
