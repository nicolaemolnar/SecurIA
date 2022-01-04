package logic;

import database.DBConnection;

import java.sql.SQLException;

public class Logic {
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
}

