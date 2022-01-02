package servlets;

import database.DBConnection;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class doRegister extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public doRegister() {
        super();
    }


    protected void doGet(HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET METHOD IS NOT ALLOWED ON THIS LEVEL OF SECURITY");
    }

    protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Obtain parameters from POST request

        // TODO Obtain connection to database
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            if (db.isConnected()) {
                // TODO: Insert user into database
            }

            // TODO: If user was inserted successfully, redirect to login page

            // TODO: If user was not inserted successfully, redirect to registration page

            // TODO Close the connection to the database
            db.closeConnection();
        }catch (Exception e){
            response.sendRedirect("/securia/error.jsp?error=database");
        }
    }
}
