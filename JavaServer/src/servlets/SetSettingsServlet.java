package servlets;

import database.DBConnection;
import logic.Log;
import logic.Logic;
import mqtt.MQTTPublisher;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class SetSettingsServlet extends HttpServlet {
    public SetSettingsServlet() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Read the request parameters
        String email = String.valueOf(request.getParameter("email"));
        String password = String.valueOf(request.getParameter("password"));
        String first_name = String.valueOf(request.getParameter("firstname"));
        String last_name = String.valueOf(request.getParameter("surname"));
        String phone_number = String.valueOf(request.getParameter("phone"));
        String birth_date = String.valueOf(request.getParameter("birthdate"));
        Boolean capturePhotos = Boolean.valueOf(request.getParameter("getPhotos"));
        Boolean sendNotifications = Boolean.valueOf(request.getParameter("sendNotifications"));
        Boolean canStream = Boolean.valueOf(request.getParameter("canStream"));

        // Create a JSON object
        JSONObject json = new JSONObject();

        // Add the response to the JSON object
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();

            json.put("success", db.setSettings(email, password, first_name, last_name, phone_number, birth_date, capturePhotos, sendNotifications, canStream));


        }catch (SQLException e) {
            Log.log.error("Error setting settings for user with email: " + email+". Cause:"+e.getMessage());
        }finally {
            db.closeConnection();
        }

        // Send the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());

        String camera_id = db.get_camera(email);
        if (canStream) {
            MQTTPublisher.publish(Logic.mqttBroker,"/sensor/camera/" + camera_id + "/canStream", "Se  dabilitado el stream de la camara: "+camera_id);
            Log.log.error("/sensor/camera/" + camera_id + "/Streaming", "True");
        }else{
            MQTTPublisher.publish(Logic.mqttBroker,"/sensor/camera/" + camera_id + "/canStream", "Se ha deshabilitado el stream de la camara: "+camera_id);


        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = String.valueOf(request.getSession().getAttribute("email"));
        String password = request.getParameter("password");
        String password_conf = request.getParameter("password_conf");
        String first_name = request.getParameter("firstname");
        String last_name = request.getParameter("surname");
        String phone_number = request.getParameter("phone");
        String birth_date = request.getParameter("birthdate");
        Boolean capturePhotos = Boolean.valueOf(request.getParameter("getPhotos"));
        Boolean sendNotifications = Boolean.valueOf(request.getParameter("sendNotifications"));
        Boolean canStream = Boolean.valueOf(request.getParameter("canStream"));

        if (password.equals(password_conf)) {
            try {
                DBConnection db = new DBConnection("postgres","123456");
                db.obtainConnection();
                if (db.setSettings(email, password, first_name, last_name, phone_number, birth_date, capturePhotos, sendNotifications, canStream)){
                    request.getSession().setAttribute("email", email);

                    String camera_idPost = db.get_camera(email);
                    if (canStream) {
                        MQTTPublisher.publish(Logic.mqttBroker,"/sensor/camera/" + camera_idPost + "/canStream", "Se  dabilitado el stream de la camara: "+camera_idPost);
                        Log.log.error("/sensor/camera/" + camera_idPost + "/Streaming", "True");
                    }else{
                        MQTTPublisher.publish(Logic.mqttBroker,"/sensor/camera/" + camera_idPost + "/canStream", "Se ha deshabilitado el stream de la camara: "+camera_idPost);


                    }

                }else{
                    request.getSession().setAttribute("error", "Some of the data provided is invalid");
                    Log.log.error("Error saving new changes for user with email: " + email);
                }
            } catch (SQLException e) {
                Log.log.error("Error obtaining connection to database");
                response.sendRedirect("/securia/error.jsp?error=database");
            }
        }else{
            request.getSession().setAttribute("error", "Passwords do not match");
        }

        response.sendRedirect("/securia/get_settings?email=" + email+"&device=web");
    }

}
