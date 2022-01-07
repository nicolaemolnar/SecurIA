package servlets;

import logic.Image;
import logic.Log;
import logic.Logic;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class GetImagesServlet extends HttpServlet {

    public GetImagesServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String device = request.getParameter("device");

        if (device.equals("android")) {
            String email = request.getParameter("email");

            ArrayList<Image> images = Logic.get_images(email);

            JSONObject json = new JSONObject();


        }else if (device.equals("web")) {
            String email = String.valueOf(request.getSession().getAttribute("email"));

            ArrayList<Image> images = Logic.get_images(email);

            ArrayList<String> paths = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            ArrayList<String> timestamps = new ArrayList<>();

            for (Image image : images) {
                paths.add(image.getPath());
                labels.add(image.getLabel());
                timestamps.add(image.getTimestamp().toString());
            }

            request.getSession().setAttribute("paths", paths);
            request.getSession().setAttribute("labels", labels);
            request.getSession().setAttribute("timestamps", timestamps);

            response.sendRedirect("/securia/gallery.jsp");
        }
    }
}
