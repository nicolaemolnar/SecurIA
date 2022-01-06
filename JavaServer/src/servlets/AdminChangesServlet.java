package servlets;

import logic.Log;
import logic.Logic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminChangesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminChangesServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean broker_activated = Boolean.parseBoolean(request.getParameter("broker_activated"));

        if (broker_activated && !Logic.mqttSubscriber.isConnected()) {
            Logic.mqttSubscriber.connectToBroker();
        }else if (!broker_activated && Logic.mqttSubscriber.isConnected()) {
            Logic.mqttSubscriber.disconnectFromBroker();
        }

        response.sendRedirect("/securia/admin.jsp");
    }
}
