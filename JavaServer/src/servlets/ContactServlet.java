package servlets;

import database.DBConnection;
import logic.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactServlet extends HttpServlet {
    public ContactServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // TODO Rediret to POST method
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Get parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String company = request.getParameter("company");
        String message = request.getParameter("message");

        Log.log.info("ContactServlet attempt:"+name+","+email+","+phone+","+company+","+message);

        boolean validContact =false;

        // Connect to database
        DBConnection db = new DBConnection("postgres","123456");
        try {
            db.obtainConnection();
        }catch (Exception e){
            response.sendRedirect("/securia/error.jsp?error=database");
        }

        // Insert contact into database
        if (db.isConnected()) {
            validContact = db.insertContact(name,email,phone,company,message);
        }

        // Redirect to confirmation page if successful
        if (validContact){
            response.sendRedirect("/securia/confirmationPage"); // Page to confirm insertion
            Log.log.info("Added contact from "+email+" to database");
        }else{
            response.sendRedirect("/securia/error.jsp?error=contact");
            Log.log.info("Failed to add contact from "+email+" to database");
        }

        // TODO Close connection to database
        db.closeConnection();
    }
}
