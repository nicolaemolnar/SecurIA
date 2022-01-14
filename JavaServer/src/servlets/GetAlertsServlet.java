package servlets;

import database.DBConnection;
import logic.Log;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class GetAlertsServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        //response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        doPost(request, response);
    }

    protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String> alerts = new ArrayList<>();
        String email = String.valueOf(request.getSession().getAttribute("email"));


        // Obtain connection to database
        DBConnection db = new DBConnection("postgres","123456");

        // Get the list of the enabled alerts linked to the email
        try {
            db.obtainConnection();

            if (db.isConnected()) {
                alerts = db.get_alerts(email);
            }
            // Close the connection to the database
            db.closeConnection();
        }
        catch (Exception e){
            response.sendRedirect("/securia/error.jsp?error=database");
        }

        int i = 0;
        // Add alerts to JSON response
        JSONObject json = new JSONObject();
        for (String alert : alerts) {
            json.put(String.valueOf(i), alert);
            i++;
        }

        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }

}