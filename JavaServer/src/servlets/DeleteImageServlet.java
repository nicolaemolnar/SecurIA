package servlets;

import database.DBConnection;
import logic.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DeleteImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteImageServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String path = request.getParameter("path");

        // TODO: delete image from database
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();
            if(db.isConnected()){
                db.delete_image(path);
            }
        }catch (Exception e){
            response.sendRedirect("/securia/error.jsp?error=database");//TODO: Especificar ruta
        }

        // delete image from file system
        File image = new File(path);
        image.delete();
        Log.log.info("Deleted image on path "+path);

        response.sendRedirect("/securia/gallery?device=web");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
