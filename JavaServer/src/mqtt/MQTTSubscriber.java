package mqtt;

import database.DBConnection;
import logic.Log;
import logic.Logic;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
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
            HashMap<String,Boolean> sensors = dbConnection.getSensors();
            ArrayList<Integer> system_ids = dbConnection.get_system_ids();
            for (String sensor_type: sensors.keySet()) {
                String topic = "/sensor/" + sensor_type + "/";
                String sensor_id = "";
                if (!sensors.get(sensor_type)) {
                    sensor_id = "#";
                }
                for (int system_id : system_ids) {
                    topics.add("/sensor/" + sensor_type + "/" + system_id +"/"+sensor_id);
                }
            }

            // Get all the camera's topics
            ArrayList<String> cameras = dbConnection.getCameras();
            for (String camera : cameras) {
                topics.add("/sensor/camera/"+camera+"/Image");
                topics.add("/sensor/camera/"+camera+"/Streaming");
                Logic.streams.put(camera, "");
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
                Log.logmqtt.info("Subscribed to topic: "+topic);
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

        // Handle the message arrival and treat data accordingly
        switch (topic) {
            case "movement":
                int sensor_id = Integer.parseInt(topics[4]);
                // Check if user allows notifications
                int system_id = Integer.parseInt(topics[3]);
                if (Logic.allows_notifications(system_id)) {
                    // If allowed, store notification in the database
                    Logic.add_alert(system_id, "Movement detected" ,"Movement detected in sensor "+sensor_id);
                    Log.logmqtt.info("Notified movement detection in sensor "+sensor_id);
                }
                break;
            case "proximity":
                system_id = Integer.parseInt(topics[3]);
                // Check if user allows notifications and the camera didn't detect any person
                if (Logic.allows_notifications(system_id) && !Logic.person_detected(system_id)) {
                    // If allowed, store notification in the database
                    Logic.add_alert(system_id, "Possible attack detected!" ,"We strongly believe your camera is being covered!!");
                    Log.logmqtt.info("Notified possible attack detected in system "+system_id);
                }

                break;
            case "camera":
                if (topics[4].equals("Image")) {
                    // Decode base64 image payload
                    String image_str = mqttMessage.toString();
                    system_id = Logic.get_system_id(topics[3]);
                    byte[] image_bytes = Base64.getDecoder().decode(image_str);
                    Log.logmqtt.info("Image received with size: " + image_bytes.length);

                    if (Logic.capture_photos(system_id)) {
                        String image_path = Logic.add_image(Logic.imagePath, image_bytes, "jpg", topics[3]);
                        Log.logmqtt.info("Image saved in: " + image_path);
                    }

                    // Check if user allows notifications
                    if (Logic.allows_notifications(system_id)) {
                        // If allowed, store notification in the database
                        Logic.add_alert(system_id, "Image received" ,"There is a person at your door.");
                        Log.logmqtt.info("Notified "+system_id+" owner for received image");
                    }
                }else if (topics[4].equals("Streaming")) {
                    String image_str = mqttMessage.toString();
                    Logic.streams.put(topics[3], image_str);
                }else if (topics[4].equals("Label")) {
                    String label_str = mqttMessage.getPayload().toString();
                    Logic.set_label(topics[3], label_str);
                }
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
