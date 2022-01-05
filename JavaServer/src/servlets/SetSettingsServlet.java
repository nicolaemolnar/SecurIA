package servlets;

import database.DBConnection;
import logic.Log;

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

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prev_email = String.valueOf(request.getSession().getAttribute("email"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String password_conf = request.getParameter("password_conf");
        String first_name = request.getParameter("firstname");
        String last_name = request.getParameter("surname");
        String phone_number = request.getParameter("phone");
        String birth_date = request.getParameter("birthdate");
        Boolean capturePhotos = Boolean.valueOf(request.getParameter("getPhotos"));
        Boolean captureVideos = Boolean.valueOf(request.getParameter("getVideos"));
        Boolean canStream = Boolean.valueOf(request.getParameter("canStream"));

        if (password.equals(password_conf)) {
            try {
                DBConnection db = new DBConnection("postgres","123456");
                db.obtainConnection();
                if (db.setSettings(prev_email, email, password, first_name, last_name, phone_number, birth_date, capturePhotos, captureVideos, canStream)){
                    response.sendRedirect("/securia/get_settings");
                }else{
                    response.sendRedirect("/securia/error.jsp?error=settings");
                    Log.log.error("Error saving new changes for user with email: " + email);
                }
            } catch (SQLException e) {
                Log.log.error("Error obtaining connection to database");
                response.sendRedirect("/securia/error.jsp?error=database");
            }
        }else{
            response.sendRedirect("/securia/error.jsp?error=settings?cause=password_conf");
        }
    }

}
