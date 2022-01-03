package servlets;

import database.DBConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.text.SimpleDateFormat;

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
        // Obtain parameters from POST request
        String username = request.getParameter("username");
	    String email = request.getParameter("email");
        String password = request.getParameter("password");
        String first_name = request.getParameter("first_name");
        String surname = request.getParameter("surname");
        String phone_number = request.getParameter("phone_number");
        String birth_date = request.getParameter("birth_date");

        Date date1 =new SimpleDateFormat("dd/MM/yyyy").parse(sDate1); 
        if password.equals(request.getParameter("password2") && date1.before(new Date())){
            // TODO Obtain connection to database
            DBConnection db = new DBConnection("postgres","123456");

            try {
                db.obtainConnection();

                if (db.isConnected()) {
                    // Insert user into database
                    if (db.register(username, first_name, email, password, surname, phone_number, birth_date)){
                        // If user was inserted successfully, redirect to login page
                        response.sendRedirect("/securia/dashboard.html"); // TODO: change path to login page's path
                    }
                    else{
                        // If user was not inserted successfully, redirect to registration page
                        response.sendRedirect("/securia/register.html"); 
                    }
                 
                }

                // Close the connection to the database
                db.closeConnection();
            }catch (Exception e){
                response.sendRedirect("/securia/error.jsp?error=database");
        }
    }
    else{response.sendRedirect(response.sendRedirect("/securia/error.jsp?error=register&cause=date_or_password");}
    }
}
