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
            request.getSession().setAttribute("error", "Error connecting to database");
            Log.logdb.error("Error connecting to database");
        }

        // Insert contact into database
        if (db.isConnected()) {
            validContact = db.insertContact(name,email,phone,company,message);
        }

        // Redirect to confirmation page if successful
        if (validContact){
            request.getSession().setAttribute("success", "Contact successfully sent");
            Log.log.info("Added contact from "+email+" to database");
        }else{
            request.getSession().setAttribute("error", "Error sending contact, information is not valid");
            Log.log.info("Failed to add contact from "+email+" to database");
        }

        // TODO Close connection to database
        db.closeConnection();

        response.sendRedirect("/securia/contact.jsp");
    }
}
