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
        MQTTBroker mqttBroker = new MQTTBroker(2, "localhost", 1883, "SecurIA Central Broker");
        MQTTSubscriber mqttSub = new MQTTSubscriber();
        MQTTPublisher mqttPub = new MQTTPublisher();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
