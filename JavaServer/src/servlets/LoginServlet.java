package servlets;

import logic.Log;
import logic.Logic;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            String username = Logic.login(email, password);

            // Create response JSON
            JSONObject json = new JSONObject();
            json.put("successful_login", !username.equals(""));

            // Send JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
        } catch (SQLException e){
            Log.log.error("Error connecting to database: " + e.getMessage());
        }
    }

    protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtain parameters from POST request
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try{
            String username = Logic.login(email, password);

            // If the user is valid, set the session attribute
            if (!username.equals("")) {
                // Redirect to the home page
                HttpSession session = request.getSession();
                session.setAttribute("email", email);
                response.sendRedirect("/securia/dashboard.jsp");
                Log.log.info("Login successful for user: " + username);
            }else{
                // If the user is not valid, redirect to the login page
                response.sendRedirect("/securia/error.jsp?error=login");
                Log.log.info("Login failed for user: " + username);
            }
        }catch (SQLException e){
            response.sendRedirect("/securia/error.jsp?error=database");
            Log.log.error("Error connecting to database: " + e.getMessage());
        }
    }
}
