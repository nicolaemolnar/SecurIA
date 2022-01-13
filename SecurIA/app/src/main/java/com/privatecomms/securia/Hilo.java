package com.privatecomms.securia;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Hilo extends Thread {

    private MqttAndroidClient client;
    private final static String CHANNEL_ID = "stationId";
    private final static int NOTIFICATION_ID=0;
    private String movement = "movement";
    private String proximity = "proximity";
    private String camera = "camera";

    private String tag = "Hilo";
    private MainActivity activity;
    private String email;

    public Hilo(MainActivity activity, String email) {
        this.activity = activity;
        this.email = email;
    }

    @Override
    public void run() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(activity.getApplicationContext(), "tcp://192.168.1.131:1883", clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //If the connection is ok
                    Log.i(tag, "MQTT connected");
                    //Suscribe the topics
                    suscripcionTopics(movement);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.i(tag, "Error connecting MQTT");
                }
            });
        } catch (MqttException e) {e.printStackTrace();}

        //Callback of MQTT to process the information received by MQTT
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {}
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception
            {
                //New alert from the wheater station
                if(topic.contains("notifications") && (topic.contains(email))){
                    String mqttText = new String(message.getPayload());
                    Log.i(tag, "New Alert: + " + (new String(message.getPayload())));

                    //Create a notification with the alert
                    //createNotificationChannel();
                    createNotification(mqttText.split(";")[0],mqttText.split(";")[1]);
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
    }

   /** //Method to create the notification channel in new versions
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "Notificación";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
**/
    //Method to create a notfication with the title and the message
    private void createNotification(String title, String msn){
        //Configure the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity.getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.securia_logo);
        builder.setContentTitle(title);
        builder.setContentText(msn);
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.BLUE, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        //Show the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(activity.getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    //MQTT topics to suscribe the application
    private void suscripcionTopics(String movement){
        try{
            Log.i(tag, "notifications = " + movement);
            client.subscribe("/android/notifications/"+email,2);


        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}
