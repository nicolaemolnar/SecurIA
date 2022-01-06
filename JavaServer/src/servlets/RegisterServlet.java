package servlets;

import database.DBConnection;
import logic.Log;
import logic.Logic;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegisterServlet() {
        super();
    }


    protected void doGet(HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        // TODO: Obtain GET parameters

        // TODO: Establish database connection

        // TODO: Call register query

        // TODO: Close database connection

        // TODO: Send response (if needed, JSON)
    }

    protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtain parameters from POST request
        String username = request.getParameter("username");
	    String email = request.getParameter("email");
        String password = request.getParameter("password");
        String first_name = request.getParameter("first_name");
        String surname = request.getParameter("surname");
        String phone_number = request.getParameter("phone_number");
        String birth_date = request.getParameter("birth_date");

        Date date1 = Date.valueOf(Logic.formatDate(birth_date));

        if (password.equals(request.getParameter("password2")) && date1.before(new Date(System.currentTimeMillis())) && !first_name.equals("admin")) {
            // Obtain connection to database
            DBConnection db = new DBConnection("postgres","123456");

            try {
                db.obtainConnection();

                if (db.isConnected()) {
                    // Insert user into database
                    if (db.register(username, first_name, email, password, surname, phone_number, date1)){
                        // If user was inserted successfully, redirect to login page
                        response.sendRedirect("/securia/index.html");
                    }
                    else{
                        // If user was not inserted successfully, redirect to error page
                        response.sendRedirect("/securia/error.jsp?error=register&cause=already_exists");
                    }
                }
                // Close the connection to the database
                db.closeConnection();
            }catch (Exception e){
                response.sendRedirect("/securia/error.jsp?error=database");
        }
    }
    else{
        response.sendRedirect("/securia/error.jsp?error=register&cause=date_or_password");
    }
    }
}
