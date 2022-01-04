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

        // TODO Get the username from the database
        // SELECT first_name from cliente where email=? and password=?
        String query = "SELECT first_name FROM \"Client\" WHERE email = '" + email + "' AND password = '" + password + "';";
        ResultSet rs = csmt.executeQuery(query);

        Log.logdb.info("Executed select from connection "+csmt.getConnection());

        if (rs.next()) {// If the table is not empty
            username = rs.getString("first_name");
            
        }

        rs.close();
        csmt.close();

        // TODO Return the username if the user exists, null string if not
        return username;
    }

    public boolean register(String username, String first_name, String email, String password, String surname, String phone, Date birth_date){
        // Prepare SQL call
        Statement csmt = null;
        boolean exito = false;

       try {
            // Begin transaction
            csmt = connection.createStatement();
            beginTransaction(csmt);

            // Insert the contact into the contacts table
            // Check if email already exists in database
            if(!csmt.executeQuery("SELECT FROM \"Client\" WHERE email = '" + email +"'").next()){
                String query = "INSERT INTO public.\"Client\" ( email, password, first_name, surname, phone_number, birth_date)"+
                "VALUES ('" + email + "', '" + password + "', '" + first_name + "', '" + surname + "', '" + phone + "', '" + birth_date + "')";
                Log.logdb.info("Inserted user with email "+email+" into database.");

                // Insert the user into the Clients table
                csmt.execute(query);  
                // Commit transaction
                closeTransaction(csmt);
                exito = true;
            }
        }catch (SQLException e) {
            cancelTransaction(csmt);
            Log.logdb.error("Error registering user "+username+"."+e.getMessage());
        }


        // Return true if successful, false if not (rollback) or email already exists in the database
        return exito;
    }

    public boolean insertContact(String name, String email, String phone, String company, String message){
        // TODO Prepare SQL call
        Statement csmt = null;

        try {
            csmt = connection.createStatement();
        // TODO Begin transaction
            beginTransaction(csmt);
        // TODO Insert the contact into the contacts table
            String query = "INSERT INTO public.Contact(name,email,phone,company,message) VALUES (name,email,phone,company,message);";
            csmt.execute(query);  //Donde capturo si no se realiza bien el insert para dev false
            Log.logdb.info("Executed insert from connection "+csmt.getConnection());
        // TODO Commit transaction
            closeTransaction(csmt);
        }catch (SQLException e) {
            cancelTransaction(csmt);
        }
        // TODO Return true if successful, false if not
        return true;
    }

    public void delete_account(String email) throws SQLException {
        // Prepare SQL call
        Statement csmt = null;

        csmt = connection.createStatement();

        // Delete the account from the database
        String query = "DELETE FROM \"Client\" WHERE email = '" + email + "';";
        csmt.execute(query);

        Log.logdb.info("Deleted account with email '"+email+" from database. Author:"+csmt.getConnection());

        csmt.close();
    }

    public ResultSet GetSensors(){
        Statement csmt = null;
        ResultSet rs = null;
        try {
            csmt = connection.createStatement();
            beginTransaction(csmt);
            String query = "SELECT * FROM \"Sensor_type\" ";
            rs = csmt.executeQuery(query);
            Log.logdb.info("Executed Select from connection "+csmt.getConnection());
            closeTransaction(csmt);
        }catch (SQLException e) {
            cancelTransaction(csmt);
        }
        return rs;

    }

    public ResultSet GetCamera(){
        Statement csmt = null;
        ResultSet rs = null;
        try {
            csmt = connection.createStatement();
            beginTransaction(csmt);
            String query = "SELECT * FROM \"Camera\" ";
            rs = csmt.executeQuery(query);
            Log.logdb.info("Executed Select from connection "+csmt.getConnection());
            closeTransaction(csmt);
        }catch (SQLException e) {
            cancelTransaction(csmt);
        }
        //como hago para que devuelva el select?
        return rs;
    }

    public HashMap<String, String> getSettings(String email){
        // Prepare SQL call
        Statement csmt = null;
        HashMap<String, String> settings = new HashMap<String, String>();

        // Get the settings from the database
        try {
            // Begin transaction
            csmt = connection.createStatement();
            beginTransaction(csmt);
            ResultSet res = csmt.executeQuery("SELECT * FROM \"Client\" natural join \"Configurations\" natural join \"System\"  " +
                    "WHERE  email =" + email + ";");
            //Close statement
            closeTransaction(csmt);
            //Store the settings in the HashMap (Tables: Client natural join Configurations natural join System)
            settings.put("email", email);
            settings.put("password", res.getString("password"));
            settings.put("firstname", res.getString("first_name"));
            settings.put("surname", res.getString("surname"));
            settings.put("phone", res.getString("phone_number"));
            settings.put("birthdate", res.getString("birth_date"));
            settings.put("getPhotos", String.valueOf(res.getBoolean("capture_photos")));
            settings.put("getVideos", String.valueOf(res.getBoolean("capture_videos")));
            settings.put("canStream", String.valueOf(res.getBoolean("live_streaming")));
        }catch (SQLException e) {
            cancelTransaction(csmt);
            Log.logdb.error("Error getting settings from user with email"+email+"."+e.getMessage());
        }

        //Return the HashMap
        return settings;
    }




}