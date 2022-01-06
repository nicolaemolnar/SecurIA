package servlets;

import logic.Log;
import logic.Logic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DeleteAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteAccountServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Delete account and proceed on mobile app
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Delete account and proceed on web app
        String email = request.getParameter("email");

        try {
            Logic.delete_account(email);
            LogoutServlet logout = new LogoutServlet();
            logout.doGet(request, response);
            Log.log.info("Account with email:"+email+" was deleted");
        } catch (SQLException e) {
            Log.log.error("Account with email:"+email+" was not deleted due to: "+e.getMessage());
        }

    }
}
