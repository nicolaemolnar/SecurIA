package mqtt;

import database.DBConnection;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

public class MQTTSubscriber implements MqttCallback {

    public void searchTopicsToSubscribe() {
        DBConnection dbConnection = new DBConnection("postgres","123456");
        ArrayList<String> topics = new ArrayList<>();
        try {
            dbConnection.obtainConnection();

            // TODO: Subscribe to all the topics the server needs

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void subscribeTopics(MQTTBroker mqttBroker, ArrayList<String> topics){
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient client = new MqttClient(mqttBroker.getBroker(), MqttClient.generateClientId(), persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options);

            client.setCallback(this);

            for (String topic : topics) {
                client.subscribe(topic);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO: Handle connection lost, reconnect to server and subscribe to topics
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String[] topics = s.split("/");

        // TODO: Handle the message arrival and treat data accordingly
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
