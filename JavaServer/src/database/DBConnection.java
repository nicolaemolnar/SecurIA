package database;

import logic.Image;
import logic.Log;
import logic.Logic;
import org.postgresql.jdbc.*;

import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DBConnection {

    // Database information
    private String dbName = "SecurIA";
    private String userName;
    private String password;
    private String hostName = "localhost";
    private String portNumber = "5432";

    // Database status
    private Connection connection;

    public DBConnection(String username, String password) {
        this.userName = username;
        this.password = password;
        Log.logdb.info("Database Connection class created");
    }

    /*  ============================== Connection Management ==============================*/
    public void obtainConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://" + hostName + ":" + portNumber + "/" + dbName, userName, password);
            Log.logdb.info("Established connection to postgresql database " + hostName + " as " + userName + ":" + connection.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.logdb.info("Error loading PostgreSQL JDBC driver");
        }


        this.connection = connection;
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    public void closeConnection() {
        try {
            connection.close();
            Log.logdb.info("Closed connection " + connection.toString() + ".");
        } catch (Exception e) {
            e.printStackTrace();
            Log.logdb.info("Error closing connection " + connection.toString() + ".");
        }
    }

    public void beginTransaction(Statement csmt) throws SQLException {
        csmt.execute("BEGIN");
        Log.logdb.info("Transaction started by connection " + csmt.getConnection());
    }

    public void cancelTransaction(Statement csmt) {
        try {
            csmt.execute("ROLLBACK");
            Log.logdb.info("Rollback from " + csmt.getConnection());
        } catch (SQLException e) {
            Log.logdb.info("Error rolling back transaction.");
        }
    }

    public void closeTransaction(Statement csmt) throws SQLException {
        csmt.execute("COMMIT");
        Log.logdb.info("Transaction committed by connection " + csmt.getConnection());
    }

    public void PreparedStatement(String sql) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            Log.logdb.info("Prepared statement " + sql + " created by connection " + connection);
        } catch (SQLException e) {
            Log.logdb.error("Error creating prepared statement:" + e.getMessage());
        }
    }

    /* ============================== SQL Calls ============================== */
    public String login(String email, String password) throws SQLException {
        // Prepare SQL call
        Statement csmt = null;
        String username = "";

        csmt = connection.createStatement();

        email = email.replace("'", "");
        password = password.replace("'", "");

        // Get the username from the database
        // SELECT first_name from cliente where email=? and password=?
        String query = "SELECT first_name FROM public.user WHERE email = '" + email + "' AND password = '" + password + "';";
        ResultSet rs = csmt.executeQuery(query);

        Log.logdb.info("Executed select from connection " + csmt.getConnection());

        if (rs.next()) {// If the table is not empty
            username = rs.getString("first_name");

        }

        rs.close();
        csmt.close();

        // Return the username if the user exists, null string if not
        return username;
    }

    public boolean register(String username, String first_name, String email, String password, String surname, String phone, Date birth_date) {
        // Prepare SQL call
        Statement csmt = null;
        boolean success = false;

        try {
            // Begin transaction
            csmt = connection.createStatement();
            beginTransaction(csmt);

            // Check if email already exists in database
            if (!csmt.executeQuery("SELECT FROM public.user WHERE email = '" + email + "'").next()) {
                // Insert new user into database
                String birth_date_str = "null";
                if (birth_date != null) {
                    birth_date_str = "'" + birth_date + "'";
                }
                String query = "INSERT INTO public.user (email, password, first_name, surname, phone_number, birth_date)" +
                        "VALUES ('" + email + "', '" + password + "', '" + first_name + "', '" + surname + "', '" + phone + "', " + birth_date_str + ")";
                csmt.execute(query);
                Log.logdb.info("Inserted user with email " + email + " into database.");

                // Create a new system configuration for the user
                int next_id = 0;
                ResultSet rs = csmt.executeQuery("SELECT MAX(system_id) from public.system");
                if (rs.next()) {
                    next_id = rs.getInt(1) + 1;
                }

                String query2 = "INSERT INTO public.system (system_id, capture_photos, send_notifications, live_streaming, email)" +
                        "VALUES (" + next_id + ", true, true, true, '" + email + "')";
                csmt.execute(query2);
                Log.logdb.info("Inserted system configuration with id " + next_id + " into database.");

                // Create new camera for the user
                int next_id2 = 0;
                ResultSet rs2 = csmt.executeQuery("SELECT MAX(camera_id) from public.camera");
                if (rs2.next()) {
                    next_id2 = Integer.parseInt(rs2.getString(1).split("_")[1]) + 1;
                }

                String query3 = "INSERT INTO public.camera (camera_id, name, detection, system_id)" +
                        "VALUES ('camera_" + next_id2 + "', '" + "Main door" + "', 'false', '" + next_id + "')";
                csmt.execute(query3);
                Log.logdb.info("Inserted camera with id " + next_id2 + " into database.");

                success = true;
            }
            // Commit transaction
            closeTransaction(csmt);
        } catch (SQLException e) {
            cancelTransaction(csmt);
            Log.logdb.error("Error registering user " + username + "." + e.getMessage());
        }


        // Return true if successful, false if not (rollback) or email already exists in the database
        return success;
    }

    public boolean insertContact(String name, String email, String phone, String company, String message) {
        // Prepare SQL call
        boolean success = false;

        try (Statement csmt = connection.createStatement()) {

            // Obtain last contact id
            ResultSet rs = csmt.executeQuery("SELECT MAX(request_id) FROM public.contact_request");

            int next_id = 0;
            if (rs.next()) {
                next_id = rs.getInt(1) + 1;
            }

            // Insert the contact into the contacts table
            String query = "INSERT INTO public.contact_request(request_id,name,email,phone_number,company_name,message) VALUES (" + next_id + ",'" + name + "','" + email + "','" + phone + "','" + company + "','" + message + "');";
            csmt.execute(query);  //Donde capturo si no se realiza bien el insert para dev false
            Log.logdb.info("Executed insert from connection " + csmt.getConnection());

            success = true;
        } catch (SQLException e) {
            success = false;
            Log.logdb.error("Error inserting contact request from " + email + "." + e.getMessage());
        }

        // Return true if successful, false if not
        return success;
    }

    public void delete_account(String email) {
        // Prepare SQL call
        Statement csmt = null;
        try {
            csmt = connection.createStatement();

            // Begin transaction
            beginTransaction(csmt);

            // Delete all the systems associated with the user
            // Obtain the systems associated with the user
            String query = "SELECT system_id FROM public.system WHERE email = '" + email + "'";
            ResultSet rs = csmt.executeQuery(query);

            ArrayList<Integer> system_ids = new ArrayList<>();
            while (rs.next()) {
                system_ids.add(rs.getInt("system_id"));
            }
            rs.close();

            for (int system_id : system_ids) {
                // Obtain all the images associated with the system
                String query2 = "SELECT * FROM public.image WHERE system_id = " + system_id;
                ResultSet rs2 = csmt.executeQuery(query2);
                while (rs2.next()){
                    int image_id = rs2.getInt("image_id");
                    // Delete image from system's storage
                    File image = new File(Logic.tomcatPath + "\\securia\\Images\\" + image_id + ".jpg");
                    image.delete();

                    // Delete image from database
                    delete_image(rs2.getString("path"));
                }

                rs2.close();

                // Delete all the cameras associated with the system
                String query3 = "DELETE FROM public.camera WHERE system_id = " + system_id;
                csmt.execute(query3);

                // Delete all the alerts associated with the system
                String query4 = "DELETE FROM public.alert WHERE system_id = " + system_id;
                csmt.execute(query4);


                String query5 = "DELETE FROM public.system WHERE system_id = " + system_id;
                csmt.execute(query5);
            }

            // Delete user's account
            String query6 = "DELETE FROM public.user WHERE email = '" + email + "'";
            csmt.execute(query6);

            // Commit transaction
            closeTransaction(csmt);
            Log.logdb.info("Deleted account with email '" + email + " from database. Author:" + csmt.getConnection());

            csmt.close();
        } catch (SQLException e) {
            cancelTransaction(csmt);
            Log.logdb.error("Error deleting account with email '" + email + "' from database." + e.getMessage());
        }
    }

    public HashMap<String, Boolean> getSensors() {
        ResultSet rs = null;
        HashMap<String, Boolean> sensors = new HashMap<>();
        try (Statement csmt = connection.createStatement()) {
            String query = "SELECT * FROM public.sensor_type; ";
            rs = csmt.executeQuery(query);
            Log.logdb.info("Executed Select from connection " + csmt.getConnection());

            while (rs.next()) {
                sensors.put(rs.getString("type"), rs.getBoolean("unique_per_system"));
            }
        } catch (SQLException e) {
            Log.logdb.error("Error getting sensors from database." + e.getMessage());
        }
        return sensors;

    }

    public ArrayList<String> getCameras() {
        ResultSet rs = null;
        ArrayList<String> cameras = new ArrayList<>();
        try (Statement csmt = connection.createStatement()) {

            String query = "SELECT camera_id FROM public.camera;";
            rs = csmt.executeQuery(query);
            Log.logdb.info("Connection " + csmt.getConnection() + " obtained all the cameras in the database.");

            while (rs.next()) {
                try {
                    cameras.add(rs.getString("camera_id"));
                } catch (SQLException e) {
                    Log.logdb.error("Error getting a camera from database. Skipping due to:" + e.getMessage());
                }
            }
        } catch (SQLException e) {
            cameras.clear();
            Log.logdb.error("Error getting cameras from database." + e.getMessage());
        }
        return cameras;
    }

    public HashMap<String, String> getSettings(String email) {
        // Prepare SQL call
        HashMap<String, String> settings = new HashMap<String, String>();

        // Get the settings from the database
        try (Statement csmt = connection.createStatement()) {
            ResultSet res = csmt.executeQuery("SELECT * FROM public.user natural join public.system " +
                    "WHERE  email ='" + email + "';");
            Log.logdb.info("Executed Select from connection " + csmt.getConnection());

            //Store the settings in the HashMap (Tables: Client natural join Configurations natural join System)
            if (res.next()) {
                settings.put("email", email);
                settings.put("password", res.getString("password"));
                settings.put("firstname", res.getString("first_name"));
                settings.put("surname", res.getString("surname"));
                settings.put("phone", res.getString("phone_number"));
                settings.put("birthdate", res.getString("birth_date"));
                settings.put("getPhotos", String.valueOf(res.getBoolean("capture_photos")));
                settings.put("sendNotifications", String.valueOf(res.getBoolean("send_notifications")));
                settings.put("canStream", String.valueOf(res.getBoolean("live_streaming")));
            }
        } catch (SQLException e) {
            Log.logdb.error("Error getting settings from user with email " + email + "." + e.getMessage());
        }

        //Return the HashMap
        return settings;
    }

    public boolean setSettings(String email, String password, String firstname, String surname, String phone, String birthdate, Boolean getPhotos, Boolean sendNotifications, Boolean canStream) {
        // Prepare SQL call
        Statement csmt = null;
        boolean success = false;

        // Set the settings in the database
        try {
            // Begin transaction
            csmt = connection.createStatement();
            beginTransaction(csmt);

            // Check if the user exists
            ResultSet res = csmt.executeQuery("SELECT * FROM public.user WHERE email = '" + email + "';");
            if (res.next()) {
                // Update user information
                String query = "UPDATE public.user SET password='" + password +
                        "', first_name='" + firstname +
                        "', surname='" + surname +
                        "', phone_number='" + phone +
                        "', birth_date='" + birthdate +
                        "' WHERE email='" + email + "';";
                csmt.execute(query);

                // Obtain system_id
                res = csmt.executeQuery("SELECT system_id FROM public.system where email='" + email + "';");
                int system_id = -1;
                if (res.next()) {
                    system_id = res.getInt("system_id");
                }

                // Update system information
                String query2 = "UPDATE public.system SET capture_photos=" + getPhotos +
                        ", send_notifications=" + sendNotifications +
                        ", live_streaming=" + canStream +
                        " WHERE system_id='" + system_id + "';";
                csmt.execute(query2);

                // Commit transaction
                closeTransaction(csmt);
                csmt.close();
                Log.logdb.info("Updated user with email " + email + " from connection " + csmt.getConnection());
                success = true;
            }
        } catch (SQLException e) {
            cancelTransaction(csmt);
            Log.logdb.error("Error setting settings for user with email " + email + "." + e.getMessage());
        }

        // Return true if successful, false if not
        return success;
    }

    public int get_image_id() {
        // Prepare SQL call
        int image_id = 0;

        try (Statement csmt = connection.createStatement()) {
            // Get the image_id
            ResultSet res = csmt.executeQuery("SELECT MAX(image_id) FROM public.image;");
            if (res.next()) {
                image_id = res.getInt("max") + 1;
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting image_id." + e.getMessage());
        }
        return image_id;
    }

    public ArrayList<Integer> get_system_ids(){
        // Prepare SQL call
        ArrayList<Integer> system_ids = new ArrayList<>();

        try (Statement csmt = connection.createStatement()) {
            // Get the image_id
            ResultSet res = csmt.executeQuery("SELECT system_id FROM public.system;");
            while (res.next()) {
                system_ids.add(res.getInt("system_id"));
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting system_ids." + e.getMessage());
        }
        return system_ids;
    }

    public void add_image(int image_id, String path, String camera_id) throws SQLException {
        // Prepare SQL call
        Statement csmt = connection.createStatement();

        // Obtain system_id associated with the camera_id
        String query = "SELECT system_id,detection FROM public.camera WHERE camera_id='" + camera_id + "';";

        ResultSet res = csmt.executeQuery(query);
        int system_id = -1;
        String label = "detected";
        if (res.next()) {
            system_id = res.getInt("system_id");
            label = res.getString("detection");
        }

        // Add the image to the database
        query = "INSERT INTO public.image (image_id,camera_id,system_id, path, label, timestamp) VALUES (" + image_id + ", '" + camera_id + "', '" + system_id + "', '" + path + "','"+label+"','" + new Timestamp(System.currentTimeMillis()) + "');";

        // Execute the query
        csmt.execute(query);

        // Close the statement
        csmt.close();
    }

    public ArrayList<Image> get_images(String email) {
        // Prepare SQL call
        ArrayList<Image> images = new ArrayList<>();

        try (Statement csmt = connection.createStatement()) {
            // Get the image_id
            ResultSet res = csmt.executeQuery("SELECT * FROM public.image natural join public.camera natural join public.system WHERE email='" + email + "' ORDER BY timestamp DESC ;");
            while (res.next()) {
                images.add(new Image(res.getString("path"),
                        res.getString("label"),
                        res.getTimestamp("timestamp")));
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting images from db. Cause:" + e.getMessage());
        }
        return images;
    }

    public boolean delete_image(String path) {
        try (Statement csmt = connection.createStatement()) {

            csmt.execute("DELETE from public.image where path = '" + path + "';");
            return true;
        } catch (SQLException e) {
            Log.logdb.error("Error deleting images from db. Cause:" + e.getMessage());
            return false;
        }
    }

    public String get_camera(String email) {
        // Prepare SQL call
        String camera_id = "";

        try (Statement csmt = connection.createStatement()) {
            // Get the image_id
            ResultSet res = csmt.executeQuery("SELECT camera_id FROM public.system natural join public.camera WHERE email='" + email + "';");
            if (res.next()) {
                camera_id = res.getString("camera_id");
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting camera_id from db. Cause:" + e.getMessage());
        }
        return camera_id;
    }

    public String execute(String query) {
        // Prepare SQL call
        String result = "| ";

        try (Statement csmt = connection.createStatement()) {
            // Get the image_id
            ResultSet res = csmt.executeQuery(query);
            for (int column = 0; column < res.getMetaData().getColumnCount(); column++) {
                result += res.getMetaData().getColumnName(column + 1) + " | ";
            }
            result += "\n";

            if (res.next()) {
                // Add the result to the string as a table
                do {
                    result += "| ";
                    for (int column = 0; column < res.getMetaData().getColumnCount(); column++) {
                        result += String.valueOf(res.getObject(column + 1)) + " | ";
                    }
                    result += "\n";
                }while (res.next());
            }
        } catch (SQLException e) {
            result = "Error executing query. Cause: "+e.getMessage();
            Log.logdb.error(result);
        }
        return result;
    }

    public ArrayList<String> get_alerts(String email){
        ArrayList<String> alerts = new ArrayList<>();
        try (Statement csmt = connection.createStatement()) {
            // Get the list of the enabled alerts linked to the email
            ResultSet res = csmt.executeQuery("SELECT * FROM public.alert NATURAL JOIN public.system WHERE email='" + email + "' ORDER BY timestamp DESC;");

            while (res.next()) {
                alerts.add(res.getInt("id")+";"+res.getString("title")+";"+res.getString("description")+";"+res.getString("timestamp"));
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting alerts from db. Cause:" +
                    e.getMessage());
        }
        return alerts;
    }

    public boolean allows_notifications(int system_id) {
        // Prepare SQL call
        boolean result = false;

        try (Statement csmt = connection.createStatement()) {
            // Get the list of the enabled alerts linked to the email
            ResultSet res = csmt.executeQuery("SELECT send_notifications FROM public.system WHERE system_id=" + system_id + ";");

            if (res.next()) {
                result = res.getBoolean("send_notifications");
            }

            res.close();

        } catch (SQLException e) {
            Log.logdb.error("Error getting alert permission from db. Cause:" +
                    e.getMessage());
        }
        return result;
    }

    public void add_alert(int system_id, String title, String description) {
        // Prepare SQL call
        try (Statement csmt = connection.createStatement()) {

            // Get the last alert_id from the database
            ResultSet res = csmt.executeQuery("SELECT MAX(id) FROM public.alert;");
            int alert_id = 1;
            if (res.next()) {
                alert_id = res.getInt("max") + 1;
            }
            res.close();

            csmt.execute("INSERT INTO public.alert (id, system_id, title, description, timestamp) VALUES (" + alert_id + "," + system_id + ", '" + title + "', '" + description + "','" + new Timestamp(System.currentTimeMillis()) + "');");

        } catch (SQLException e) {
            Log.logdb.error("Error adding alert to db. Cause:" + e.getMessage());
        }
    }

    public boolean person_detected(int system_id) {
        // Prepare SQL call
        boolean result = false;

        try (Statement csmt = connection.createStatement()) {
            // Get the list of the enabled alerts linked to the email
            ResultSet res = csmt.executeQuery("SELECT * FROM public.alert WHERE system_id=" + system_id + ";");

            LocalDateTime current_time = LocalDateTime.now();
            // Look for the alert with closes timestamp to current time
            while (res.next() && !result) {
                LocalDateTime alert_time = res.getTimestamp("timestamp").toLocalDateTime();
                String alert_title = res.getString("title");

                // Obtain the seconds between the current time and the alert time
                long seconds = ChronoUnit.SECONDS.between(alert_time, current_time);

                if (alert_title.contains("Image") && seconds < 3) {
                    result = true;
                }

            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting alert permission from db. Cause:" +
                    e.getMessage());
        }
        return result;
    }

    public int get_system_id(String camera_id) {
        // Prepare SQL call
        int result = 0;

        try (Statement csmt = connection.createStatement()) {
            // Get the list of the enabled alerts linked to the email
            ResultSet res = csmt.executeQuery("SELECT system_id FROM public.camera WHERE camera_id='" + camera_id + "';");

            if (res.next()) {
                result = res.getInt("system_id");
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting system_id from db. Cause:" +
                    e.getMessage());
        }
        return result;
    }

    public boolean capture_photos(int system_id) {
        // Prepare SQL call
        boolean result = false;

        try (Statement csmt = connection.createStatement()) {
            // Get the list of the enabled alerts linked to the email
            ResultSet res = csmt.executeQuery("SELECT capture_photos FROM public.system WHERE system_id=" + system_id + ";");

            if (res.next()) {
                result = res.getBoolean("capture_photos");
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting capture_photos from db. Cause:" +
                    e.getMessage());
        }
        return result;
    }

    public void delete_alert(int id) {
        // Prepare SQL call
        try (Statement csmt = connection.createStatement()) {
            csmt.execute("DELETE FROM public.alert WHERE id=" + id + ";");
        } catch (SQLException e) {
            Log.logdb.error("Error deleting alert from db. Cause:" +
                    e.getMessage());
        }
    }

    public String get_label(String camera_id) {
        // Prepare SQL call
        String result = "";

        try (Statement csmt = connection.createStatement()) {
            // Get the list of the enabled alerts linked to the email
            ResultSet res = csmt.executeQuery("SELECT detection FROM public.camera WHERE camera_id='" + camera_id + "';");

            if (res.next()) {
                result = res.getString("detection");
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting label from db. Cause:" +
                    e.getMessage());
        }
        return result;
    }

    public void set_label(String camera_id, String label) {
        // Prepare SQL call
        try (Statement csmt = connection.createStatement()) {
            csmt.execute("UPDATE public.camera SET detection='" + label + "' WHERE camera_id='" + camera_id + "';");
        } catch (SQLException e) {
            Log.logdb.error("Error setting label from db. Cause:" +
                    e.getMessage());
        }
    }

    public String get_user_email(int system_id) {
        // Prepare SQL call
        String result = "";

        try (Statement csmt = connection.createStatement()) {
            // Get the list of the enabled alerts linked to the email
            ResultSet res = csmt.executeQuery("SELECT email FROM public.system WHERE system_id='" + system_id + "';");

            if (res.next()) {
                result = res.getString("email");
            }

        } catch (SQLException e) {
            Log.logdb.error("Error getting email from db. Cause:" +
                    e.getMessage());
        }
        return result;
    }
}
