package logic;

import database.DBConnection;
import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSubscriber;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Logic {
    public static String imagePath = "/securia/Images/";
    public static MQTTBroker mqttBroker;
    public static MQTTSubscriber mqttSubscriber;
    public static MQTTPublisher mqttPublisher;
    public static HashMap<String, String[]> streams;

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
}

