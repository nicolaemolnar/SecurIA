package servlets;

import database.DBConnection;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
        String username = "";


        // TODO Obtain connection to database
        DBConnection db = new DBConnection("postgres","123456");
        db.obtainConnection();

        if (db.isConnected()) {
            // TODO Check if the user is valid
            username = db.login(email, password);
        }

        // TODO If the user is valid, set the session attribute
        if (!username.isEmpty()) {
            // TODO Redirect to the home page
            response.sendRedirect("/securia/dashboard.html");
        }else{
            // TODO If the user is not valid, redirect to the login page
            response.sendRedirect("/securia/index.html");
        }

        // TODO Close the connection to the database
        db.closeConnection();
    }

}
