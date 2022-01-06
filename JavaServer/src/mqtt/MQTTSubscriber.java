package mqtt;

import database.DBConnection;
import logic.Log;
import logic.Logic;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class MQTTSubscriber implements MqttCallback {
    private MQTTBroker broker;
    private MqttClient client;

    public MQTTSubscriber(MQTTBroker mqttBroker) {
        broker = mqttBroker;
        connectToBroker();
    }

    public void searchTopicsToSubscribe() {
        DBConnection dbConnection = new DBConnection("postgres","123456");
        ArrayList<String> topics = new ArrayList<>();
        try {
            dbConnection.obtainConnection();

            // Example topics: "sensor/movement/1", "sensor/presence/1", "sensor/camera/1/Image", "sensor/camera/1/Streaming"

            // Get all the sensor's topics
            HashMap<String,Integer> sensors = dbConnection.getSensors();
            for (String sensor : sensors.keySet()) {
                topics.add("/sensor/"+sensor+"/"+sensors.get(sensor));
            }

            // Get all the camera's topics
            ArrayList<String> cameras = dbConnection.getCameras();
            for (String camera : cameras) {
                topics.add("/sensor/camera/"+camera+"/Image");
                topics.add("/sensor/camera/"+camera+"/Streaming");
            }

            // Subscribe to all the topics the server needs
            subscribeTopics(topics);
        }catch (Exception e){
            Log.logmqtt.error("Error searching topics to subscribe. Cause:"+e.getMessage());
        }
    }

    public void connectToBroker() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(broker.getBroker(), broker.getClientId(), persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options);

            client.setCallback(this);
        }catch (MqttException e){
            Log.logmqtt.error("Error connecting to broker. Cause:"+e.getMessage());
        }
    }

    public void subscribeTopics(ArrayList<String> topics){
        try {
            for (String topic : topics) {
                client.subscribe(topic);
                //Log.logmqtt.error("Subscribed to topic: "+topic);
            }
        }catch (Exception e){
            Log.logmqtt.error("Error subscribing to topics. Cause:"+e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // Handle connection lost, reconnect to server and subscribe to topics
        Log.logmqtt.error("Connection lost, reconnecting to server and resuscribing to topics. Cause:"+cause.getCause().getMessage());
        connectToBroker();
        searchTopicsToSubscribe();
    }

    @Override
    public void messageArrived(String path, MqttMessage mqttMessage) throws Exception {
        String[] topics = path.split("/");
        //Log.logmqtt.error("Message arrived: "+mqttMessage.toString());

        String topic = topics[2];

        // TODO: Handle the message arrival and treat data accordingly
        switch (topic) {
            case "movement":
                int measurement = Integer.parseInt(mqttMessage.getPayload().toString());
                int sensor_id = Integer.parseInt(topics[3]);
                // TODO: Save the measurement in the database
                break;
            case "camera":
                String image_str = mqttMessage.toString();
                byte[] image_bytes = Base64.getDecoder().decode(image_str);
                Log.logmqtt.error("Image received: "+image_bytes.length);
                String image_path = Logic.add_image(Logic.imagePath, image_bytes, "jpg");
                Log.logmqtt.error("Image saved in: "+image_path);
                break;
            default:
                Log.logmqtt.error("Topic not recognized: "+topic);
                break;

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.logmqtt.info("Message delivered successfully");
    }

    public void disconnectFromBroker(){
        try {
            client.disconnect();
        }catch (Exception e){
            Log.logmqtt.error("Error disconnecting from broker. Cause:"+e.getMessage());
        }
    }

    public boolean isConnected(){
        return client.isConnected();
    }
}
