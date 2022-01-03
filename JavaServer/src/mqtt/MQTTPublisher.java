package mqtt;


import logic.Log;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPublisher {

    public static void publish(MQTTBroker broker, String topic, String message) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            // TODO: Connect to MQTT Broker
            MqttClient client = new MqttClient(broker.getBroker(), broker.getClientId(), persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            Log.logmqtt.info("Publisher connected to MQTT Broker");

            // TODO: Prepare message to publish
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(broker.getQos());

            // TODO: Publish message and disconnect
            client.publish(topic, mqttMessage);
            Log.logmqtt.info("Published message to topic: " + topic);
            client.disconnect();
            Log.logmqtt.info("Disconnected from MQTT Broker");
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
