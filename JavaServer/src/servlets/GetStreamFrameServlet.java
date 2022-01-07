package servlets;

import database.DBConnection;
import logic.Log;
import logic.Logic;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetStreamFrameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetStreamFrameServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        String[] stream = null;
        JSONObject json = new JSONObject();
        // Obtain connection to the database
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();
            String camera_id = db.get_camera(email);

            stream = Logic.streams.get(camera_id);
            if (stream != null) {
                json.put("stream", stream[0]);
                json.put("label", stream[1]);
            }
        }catch (SQLException e){
            Log.log.error("Error obtaining stream frame from server. Cause:"+e.getMessage());
        }finally {
            db.closeConnection();
        }

        json.put("success", stream != null);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
