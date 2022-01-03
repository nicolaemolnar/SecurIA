package servlets;

import database.DBConnection;
import logic.Log;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class doLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public doLogin() {
        super();
    }


    protected void doGet(HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET METHOD IS NOT ALLOWED ON THIS LEVEL OF SECURITY");
    }

    protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtain parameters from POST request
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Log.log.info("Login attempt with email: " + email);

        String username = "";

        // Obtain connection to database
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            if (db.isConnected()) {
                // Check if the user is valid
                username = db.login(email, password);
            }

            // If the user is valid, set the session attribute
            if (!username.equals("")) {
                // Redirect to the home page
                response.sendRedirect("/securia/dashboard.html");
                Log.log.info("Login successful for user: " + username);
            }else{
                // If the user is not valid, redirect to the login page
                response.sendRedirect("/securia/error.jsp?error=login");
                Log.log.info("Login failed for user: " + username);
            }

            // Close the connection to the database
            db.closeConnection();
        }catch (Exception e){
            response.sendRedirect("/securia/error.jsp?error=database");
            Log.log.error("Error connecting to database: " + e.getMessage());
        }
    }
}
