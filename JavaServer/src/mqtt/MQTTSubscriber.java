package mqtt;

import database.DBConnection;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class MQTTSubscriber implements MqttCallback {

    public void searchTopicsToSubscribe(MQTTBroker mqttBroker) {
        DBConnection dbConnection = new DBConnection("postgres","123456");
        ArrayList<String> topics = new ArrayList<>();
        try {
            dbConnection.obtainConnection();
            HashMap<String,Integer> sensores = dbConnection.GetSensors();
            for (String sensor : sensores.keySet()) {
                topics.add("/sensor/"+sensor+"/"+sensores.get(sensor));
            }
            subscribeTopics(mqttBroker, topics);


            // TODO: Subscribe to all the topics the server needs
            // Topics ejemplo: "sensor/movement/1", "sensor/presence/1"

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

        String topic = topics[1];

        // TODO: Handle the message arrival and treat data accordingly
        switch (topic){
            case "movement":
                int measurement = Integer.parseInt(mqttMessage.getPayload().toString());
                int sensor_id = Integer.parseInt(topics[2]);
                // TODO: Save the measurement in the database
                break;
            default:
                break;

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
