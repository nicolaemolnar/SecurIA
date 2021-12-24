package database;

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
    }

    public void obtainConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://" + hostName + ":" + portNumber + "/" + dbName, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.connection = connection;
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beginTransaction(Statement csmt) throws SQLException {
        csmt.execute("BEGIN");
    }

    public void cancelTransaction(Statement csmt) {
        try{
            csmt.execute("ROLLBACK");
        } catch (SQLException e) {
            System.out.println("FATAL ERROR CANCELING TRANSACTION");
        }
    }

    public void closeTransaction(Statement csmt) throws SQLException {
        csmt.execute("COMMIT");
    }

    /* ============================== SQL Calls ============================== */
    public String login(String email, String password) {
        // TODO Prepare SQL call
        Statement csmt = null;
        String username = "";

        try {
            csmt = connection.createStatement();

            // TODO Get the username from the database
            beginTransaction(csmt);
            String query = "SELECT first_name FROM Client WHERE email = '" + email + "' AND password = '" + password + "'";
            ResultSet rs = csmt.executeQuery(query);

            if (rs.next()) {// If the table is not empty
                username = rs.getString("first_name");
            }
            closeTransaction(csmt);

        }catch (SQLException e) {
           cancelTransaction(csmt);
        }
        // TODO Return the username if the user exists, null string if not
        return username;
    }

    public boolean insertContact(String name, String email, String phone, String company, String message){
        // TODO Prepare SQL call
        Statement csmt = null;

        try {
            csmt = connection.createStatement();
        // TODO Begin transaction
            beginTransaction(csmt);
        // TODO Insert the contact into the contacts table
            String query = "INSERT INTO public.Contact(name,email,phone,company,message) VALUES (name,email,phone,company,masasage);";
            csmt.execute(query);  //Donde capturo si no se realiza bien el insert para dev false
        // TODO Commit transaction
            closeTransaction(csmt);
        }catch (SQLException e) {
            cancelTransaction(csmt);
        }
        // TODO Return true if successful, false if not
        return true;
    }
}