package logic;

import database.DBConnection;
import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSubscriber;

import java.io.*;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Logic {
    public static String imagePath = "/securia/Images/";
    public static String dbBin = "C:\\Program Files\\PostgreSQL\\14\\bin\\";
    public static String dbPath = "C:\\Program Files\\PostgreSQL\\14\\data\\";
    public static String tomcatPath = "C:\\xampp\\tomcat\\webapps";
    public static MQTTBroker mqttBroker;
    public static MQTTSubscriber mqttSubscriber;
    public static HashMap<String, String> streams;

    public static String formatDate(String date) {
        String[] dateSplit = date.split("-");
        if (dateSplit[0].length() > 2) {
            return date;
        } else {
            return dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0];
        }
    }

    public static String login(String email, String password) throws SQLException {
        Log.log.info("Login attempt with email: " + email);

        String username = "";

        // Obtain connection to
        DBConnection db = new DBConnection("postgres","123456");
        db.obtainConnection();

        if (db.isConnected()) {
            // Check if the user is valid
            username = db.login(email, password);
        }
        // Close the connection to the database
        db.closeConnection();

        return username;
    }

    public static void delete_account(String email) throws SQLException {
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");

        db.obtainConnection();

        // Delete account from db
        db.delete_account(email);

        // Close DB connection
        db.closeConnection();
    }

    public static String add_image(String path, byte[] image, String extension, String camera_id) {
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        String file_path = null;
        try {
            db.obtainConnection();

            // Obtain image id
            int image_id = db.get_image_id();

            // Add image to file system
            file_path = path + image_id + "." + extension;
            String tomcat_path = System.getProperty("catalina.base") + "/webapps/" + file_path;
            File image_file = new File(tomcat_path);
            if (!image_file.exists()) {
                try {
                    image_file.createNewFile();
                } catch (Exception e) {
                    Log.log.error("Could not create image: " + file_path+". Cause: " + e.getMessage());
                }
            }

            try{
                // Write image to file
                FileOutputStream fos = new FileOutputStream(image_file);
                fos.write(image);
                fos.close();
                Log.log.info("Image added to file system");
            }catch (Exception e) {
                Log.log.error("Could not write image to file: " + file_path+". Cause: " + e.getMessage());
            }

            // Add image to db
            db.add_image(image_id, file_path, camera_id);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not add image to db. Cause:" + e.getMessage());
        }

        return file_path;
    }

    public static ArrayList<Image> get_images(String email) {
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        ArrayList<Image> images = new ArrayList<>();
        try {
            db.obtainConnection();

            // Get images from db
            images = db.get_images(email);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not get images from db for user "+email+". Cause:" + e.getMessage());
        }

        return images;
    }

    public static boolean allows_notifications(int system_id) {
        boolean allow_notifications = false;
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Check if system allows notifications
            allow_notifications = db.allows_notifications(system_id);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not check if system allows notifications. Cause:" + e.getMessage());
        }
        return allow_notifications;
    }

    public static void add_alert(int system_id, String title, String description) {
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Add alert to db
            db.add_alert(system_id, title, description);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not add alert to db. Cause:" + e.getMessage());
        }

        // Publisher for android notifications
        // Obtain user email from db
        String email = "";
        try {
            db.obtainConnection();

            // Get user email
            email = db.get_user_email(system_id);

            if (email != "") {
                // Send notification to email
                MQTTPublisher.publish(Logic.mqttBroker, "/android/notifications/"+email, title + "; " + description);
            }

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.log.error("Could not get user email. Cause:" + e.getMessage());
        }
    }

    public static boolean person_detected(int system_id) {
        boolean person_detected = false;
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Check if person detected
            person_detected = db.person_detected(system_id);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not check if person detected. Cause:" + e.getMessage());
        }
        return person_detected;
    }

    public static int get_system_id(String camera_id) {
        int system_id = -1;
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Get system id
            system_id = db.get_system_id(camera_id);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not get system id. Cause:" + e.getMessage());
        }
        return system_id;
    }

    public static boolean capture_photos(int system_id) {
        boolean capture_photos = false;
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Check if person detected
            capture_photos = db.capture_photos(system_id);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not check if person detected. Cause:" + e.getMessage());
        }
        return capture_photos;
    }

    public static void delete_alert(int id) {
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Delete alert
            db.delete_alert(id);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not delete alert. Cause:" + e.getMessage());
        }
    }

    public static String get_label(String camera_id) {
        String label = "";
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Get label
            label = db.get_label(camera_id);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not get label. Cause:" + e.getMessage());
        }
        return label;
    }

    public static void set_label(String camera_id, String label) {
        // Obtain DB connection
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            // Set label
            db.set_label(camera_id, label);

            // Close DB connection
            db.closeConnection();
        }catch (SQLException e){
            Log.logdb.error("Could not set label. Cause:" + e.getMessage());
        }
    }

    public static void toggleStream(Boolean canStream, String email, DBConnection db) {
        String camera_id = db.get_camera(email);
        if (canStream) {
            MQTTPublisher.publish(Logic.mqttBroker,"/sensor/camera/" + camera_id + "/canStream", "True");
        }else{
            MQTTPublisher.publish(Logic.mqttBroker,"/sensor/camera/" + camera_id + "/canStream", "False");
        }
    }
}

