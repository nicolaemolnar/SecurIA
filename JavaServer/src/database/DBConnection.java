package database;

import logic.Log;
import org.postgresql.jdbc.*;

import java.sql.*;

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

    /* ============================== SQL Calls ============================== */
    public String login(String email, String password) throws SQLException {
        // Prepare SQL call
        Statement csmt = null;
        String username = "";

        csmt = connection.createStatement();

        email = email.replace("'", "");
        password = password.replace("'", "");

        // TODO Get the username from the database
        String query = "SELECT first_name FROM \"Client\" WHERE email = '" + email + "' AND password = '" + password + "'";
        ResultSet rs = csmt.executeQuery(query);
        csmt.close();
        Log.logdb.info("Executed select from connection "+csmt.getConnection());

        if (rs.next()) {// If the table is not empty
            username = rs.getString("first_name");
        }

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
            if(!csmt.executeQuery("SELECT FROM \"Client\" WHERE email = '" + email +"' OR username = '" + username + "'").next()){
                String query = "INSERT INTO public.\"Client\" ( email, password, first_name, surname, phone_number, birth_date)"+
                "VALUES ('" + email + "', '" + password + "', '" + first_name + "', '" + surname + "', '" + phone + "', '" + birth_date + "')";
                // Insert the user into the Clients table
                csmt.execute(query);  
                // Commit transaction
                closeTransaction(csmt);
                exito = true;
            }
            
        }catch (SQLException e) {
            cancelTransaction(csmt);
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

}