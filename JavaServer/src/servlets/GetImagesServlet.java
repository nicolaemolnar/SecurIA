package servlets;

import logic.Image;
import logic.Logic;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

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
            int img_cont = 0;

            // Open every image, read it, convert it to base64 and insert it into the JSON
            for (Image image : images) {
                // Transform img path to absolute path
                String img_partial_path = image.getPath();
                img_partial_path.replace('/', '\\');
                // \\securia\\images\\1.jpg
                String img_path = Logic.tomcatPath + img_partial_path;

                String base64;
                // Open the image from the path
                File img = new File(img_path);
                if (img.exists()) {
                    // Read img file bytes array
                    byte[] fileContent = Files.readAllBytes(img.toPath());

                    // Encode the base64 string
                    base64 = Base64.getEncoder().encodeToString(fileContent);

                    // Insert the base64 into the JSON, every hashmap represents an image object
                    ArrayList<String> map = new ArrayList<>();
                    map.add(base64);
                    map.add(image.getLabel());
                    map.add(String.valueOf(image.getTimestamp()));

                    json.put(String.valueOf(img_cont++), map);
                }
            }

            json.put("img_count", String.valueOf(img_cont));

            // Return the JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
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
