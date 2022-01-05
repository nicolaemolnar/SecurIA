package database;

import logic.Log;
import org.postgresql.jdbc.*;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
            Log.logdb.info("Established connection to postgresql database "+hostName+" as "+userName+":"+connection.toString());
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
            Log.logdb.info("Closed connection "+connection.toString()+".");
        } catch (Exception e) {
            e.printStackTrace();
            Log.logdb.info("Error closing connection "+connection.toString()+".");
        }
    }

    public void beginTransaction(Statement csmt) throws SQLException {
        csmt.execute("BEGIN");
        Log.logdb.info("Transaction started by connection "+csmt.getConnection());
    }

    public void cancelTransaction(Statement csmt) {
        try{
            csmt.execute("ROLLBACK");
            Log.logdb.info("Rollback from "+csmt.getConnection());
        } catch (SQLException e) {
            Log.logdb.info("Error rolling back transaction.");
        }
    }

    public void closeTransaction(Statement csmt) throws SQLException {
        csmt.execute("COMMIT");
        Log.logdb.info("Transaction committed by connection "+csmt.getConnection());
    }

    public void PreparedStatement (String sql) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            Log.logdb.info("Prepared statement " + sql + " created by connection " + connection);
        } catch (SQLException e) {
            Log.logdb.error("Error creating prepared statement:"+e.getMessage());
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

        Log.logdb.info("Executed select from connection "+csmt.getConnection());

        if (rs.next()) {// If the table is not empty
            username = rs.getString("first_name");
            
        }

        rs.close();
        csmt.close();

        // Return the username if the user exists, null string if not
        return username;
    }

    public boolean register(String username, String first_name, String email, String password, String surname, String phone, Date birth_date){
        // Prepare SQL call
        Statement csmt = null;
        boolean success = false;

       try {
            // Begin transaction
            csmt = connection.createStatement();
            beginTransaction(csmt);

            // Check if email already exists in database
            if(!csmt.executeQuery("SELECT FROM public.user WHERE email = '" + email +"'").next()){
                // Insert new user into database
                String query = "INSERT INTO public.user (email, password, first_name, surname, phone_number, birth_date)"+
                "VALUES ('" + email + "', '" + password + "', '" + first_name + "', '" + surname + "', '" + phone + "', '" + birth_date + "')";
                csmt.execute(query);
                Log.logdb.info("Inserted user with email "+email+" into database.");

                // Create a new system configuration for him
                int next_id = 0;
                ResultSet rs = csmt.executeQuery("SELECT MAX(system_id) from public.system");
                if (rs.next()) {
                    next_id = rs.getInt(1) + 1;
                }

                String query2 = "INSERT INTO public.system (system_id, capture_photos, capture_videos, live_streaming)" +
                        "VALUES (" + next_id + ", true, true, true)";
                csmt.execute(query2);
                Log.logdb.info("Inserted system configuration with id "+next_id+" into database.");

                String relation = "INSERT INTO public.configurations (email, system_id)"+
                "VALUES ('" + email + "', '" + next_id + "')";
                csmt.execute(relation);
                Log.logdb.info("Inserted relation between user and system configuration into database.");

                success = true;
            }
           // Commit transaction
           closeTransaction(csmt);
        }catch (SQLException e) {
            cancelTransaction(csmt);
            Log.logdb.error("Error registering user "+username+"."+e.getMessage());
        }


        // Return true if successful, false if not (rollback) or email already exists in the database
        return success;
    }

    public boolean insertContact(String name, String email, String phone, String company, String message){
        // Prepare SQL call
        boolean success = false;

        try (Statement csmt = connection.createStatement()){

            // Obtain last contact id
            ResultSet rs = csmt.executeQuery("SELECT MAX(request_id) FROM public.contact_request");

            int next_id = 0;
            if (rs.next()) {
                next_id = rs.getInt(1) + 1;
            }

            // Insert the contact into the contacts table
            String query = "INSERT INTO public.contact_request(request_id,name,email,phone_number,company_name,message) VALUES ("+next_id+",'"+name+"','"+email+"','"+phone+"','"+company+"','"+message+"');";
            csmt.execute(query);  //Donde capturo si no se realiza bien el insert para dev false
            Log.logdb.info("Executed insert from connection "+csmt.getConnection());

            success = true;
        }catch (SQLException e) {
            success = false;
            Log.logdb.error("Error inserting contact request from "+email+"."+e.getMessage());
        }

        // Return true if successful, false if not
        return success;
    }

    public void delete_account(String email) throws SQLException {
        // Prepare SQL call
        Statement csmt = null;

        csmt = connection.createStatement();

        // Delete the account from the database
        String query = "DELETE FROM public.user WHERE email = '" + email + "';";
        csmt.execute(query);

        Log.logdb.info("Deleted account with email '"+email+" from database. Author:"+csmt.getConnection());

        csmt.close();
    }

    public HashMap<String, Integer> GetSensors(){
        ResultSet rs = null;
        HashMap<String, Integer> sensors = new HashMap<>();
        try (Statement csmt = connection.createStatement()) {
            String query = "SELECT * FROM public.sensor_type; ";
            rs = csmt.executeQuery(query);
            Log.logdb.info("Executed Select from connection "+csmt.getConnection());

            while (rs.next()) {
                sensors.put(rs.getString("name"), rs.getInt("type"));
            }
        }catch (SQLException e) {
            Log.logdb.error("Error getting sensors from database."+e.getMessage());
        }
        return sensors;

    }

    public ArrayList<String> GetCameras(){
        ResultSet rs = null;
        ArrayList<String> cameras = new ArrayList<>();
        try (Statement csmt = connection.createStatement()) {

            String query = "SELECT * FROM public.camera; ";
            rs = csmt.executeQuery(query);
            Log.logdb.info("Executed Select from connection "+csmt.getConnection());

            while (rs.next()) {
                cameras.add(rs.getString("name"));
            }

        }catch (SQLException e) {
            Log.logdb.error("Error getting cameras from database."+e.getMessage());
        }
        // como hago para que devuelva el select?
        return cameras;
    }

    public HashMap<String, String> getSettings(String email){
        // Prepare SQL call
        HashMap<String, String> settings = new HashMap<String, String>();

        // Get the settings from the database
        try (Statement csmt = connection.createStatement()) {
            ResultSet res = csmt.executeQuery("SELECT * FROM public.user natural join public.configurations natural join public.system " +
                    "WHERE  email ='"+email+"';");
            Log.logdb.info("Executed Select from connection "+csmt.getConnection());

            //Store the settings in the HashMap (Tables: Client natural join Configurations natural join System)
            if (res.next()) {
                settings.put("email", email);
                settings.put("password", res.getString("password"));
                settings.put("firstname", res.getString("first_name"));
                settings.put("surname", res.getString("surname"));
                settings.put("phone", res.getString("phone_number"));
                settings.put("birthdate", res.getString("birth_date"));
                settings.put("getPhotos", String.valueOf(res.getBoolean("capture_photos")));
                settings.put("getVideos", String.valueOf(res.getBoolean("capture_videos")));
                settings.put("canStream", String.valueOf(res.getBoolean("live_streaming")));
            }
        }catch (SQLException e) {
            Log.logdb.error("Error getting settings from user with email "+email+"."+e.getMessage());
        }

        //Return the HashMap
        return settings;
    }
}