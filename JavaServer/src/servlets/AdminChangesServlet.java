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
        Boolean broker_activated = Boolean.parseBoolean(request.getParameter("mqtt"));
        Boolean db_activated = Boolean.parseBoolean(request.getParameter("database"));

        if (broker_activated && !Logic.mqttSubscriber.isConnected()) {
            Logic.mqttSubscriber.connectToBroker();
            Log.logmqtt.error("MQTT Subscriber connected by admin");
        }else if (!broker_activated && Logic.mqttSubscriber.isConnected()) {
            Logic.mqttSubscriber.disconnectFromBroker();
            Log.logmqtt.error("MQTT Subscriber disconnected by admin");
        }

        if (db_activated && !Logic.is_db_connected()) {
            Logic.update_postgres_state("start");
            Log.logdb.error("Postgres started by admin");
        }else if(!db_activated && Logic.is_db_connected()){
            Logic.update_postgres_state("stop");
            Log.logdb.error("Postgres stopped by admin");
        }

        response.sendRedirect("/securia/admin.jsp");
    }
}
