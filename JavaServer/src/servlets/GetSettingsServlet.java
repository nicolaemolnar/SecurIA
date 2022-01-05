package servlets;

import database.DBConnection;
import logic.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

public class GetSettingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetSettingsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get settings from database and add them to session
        // Get parameters
        String email = String.valueOf(request.getSession().getAttribute("email"));
        // Obtain db connection
        DBConnection db = new DBConnection("postgres","123456");
        // Get settings for user with received email
        try {
            db.obtainConnection();

            if (db.isConnected()) {
                // Insert user into database
                HashMap<String, String> settings = db.getSettings(email);
                if (!settings.isEmpty()){
                    // If user was inserted successfully, redirect to login page
                    // Add settings to session
                    HttpSession session = request.getSession();
                    for (String key : settings.keySet()) {
                        session.setAttribute(key, settings.get(key));
                    }

                    // Redirect to settings page (.jsp)
                    response.sendRedirect("/securia/settings.jsp");
                    Log.log.info("Settings obtained successfully for user with email: " + email);
                }
                else{
                    // If user was not inserted successfully, redirect to error page
                    response.sendRedirect("/securia/error.jsp?error=settings");
                    Log.log.error("The user with email: " + email + " is not related to any settings");
                }
            }
            // Close the connection to the database
            db.closeConnection();

        }catch (Exception e){
            response.sendRedirect("/securia/error.jsp?error=database");
            Log.log.error("Could not obtain settings for user with email: " + email+". Error: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}