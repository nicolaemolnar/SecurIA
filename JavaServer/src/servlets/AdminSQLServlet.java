package servlets;

import database.DBConnection;
import logic.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminSQLServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminSQLServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Show a message of "not allowed method"
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String result = "";
        // Check if the author is the admin
        if (request.getSession().getAttribute("email").equals("admin@securia.com")) {
            // Get the SQL query
            String query = request.getParameter("query");

            // Obtain database connection
            DBConnection dbConnection = new DBConnection("postgres", "123456");

            try{
                dbConnection.obtainConnection();

                // Execute the query
                result = dbConnection.execute(query);

            }catch (SQLException e){
                result = "Could not obtain database connection. Cause: " + e.getMessage();
                Log.logdb.error(result);
            }finally {
                // Close the connection
                dbConnection.closeConnection();
            }

            // Return the result to the client
            response.getWriter().write(result);
        }
    }
}
