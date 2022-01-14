package servlets;

import database.DBConnection;
import logic.Log;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class GetSettingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetSettingsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, String> settings = new HashMap<>();
        // Get settings from database and add them to session
        // Get parameters
        String email = String.valueOf(request.getParameter("email"));
        String device = String.valueOf(request.getParameter("device"));
        // Obtain db connection
        DBConnection db = new DBConnection("postgres","123456");

        // Get settings for user with received email
        try {
            db.obtainConnection();

            if (db.isConnected()) {
                // Insert user into database
                settings = db.getSettings(email);
            }
            // Close the connection to the database
            db.closeConnection();
        } catch (SQLException e){
            Log.logdb.error("Could not obtain settings for user with email: " + email+". Error: " + e.getMessage());
        }


        // Differentiate between Web and Android request
        if (device.equals("android")) {
            // Add settings to JSON
            if (!settings.isEmpty()) {
                JSONObject json = new JSONObject(settings);

                /*for (String key : settings.keySet()) {
                    json.put(key,settings.get(key));
                }*/

                // Send JSON to client
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json.toString());
            } else {
                // If no settings were found, return error
                response.sendRedirect("/securia/error.jsp?error=settings");
                Log.log.error("The user with email: " + email + " is not related to any settings");
            }
        } else if(device.equals("web")){
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
                // If a database error occurs, redirect to error page
                response.sendRedirect("/securia/error.jsp?error=settings");
                Log.log.error("The user with email: " + email + " is not related to any settings");
            }
        } else {
            // If the device is not recognized, return an error
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Device "+device+" not recognized");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
