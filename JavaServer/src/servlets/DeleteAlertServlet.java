package servlets;

import logic.Log;
import logic.Logic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteAlertServlet extends HttpServlet {
    public void DeleteAlertServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int alertId = Integer.parseInt(request.getParameter("id"));
        Logic.delete_alert(alertId);
        response.getWriter().write("success");
    }
}
