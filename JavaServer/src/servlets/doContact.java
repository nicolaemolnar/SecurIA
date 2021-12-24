package servlets;

import database.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class doContact extends HttpServlet {
    public doContact() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // TODO implement doGet
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Get parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String company = request.getParameter("company");
        String message = request.getParameter("message");
        boolean validContact =false;

        // TODO Connect to database
        DBConnection db = new DBConnection("postgres","123456");
        db.obtainConnection();

        // TODO Insert contact into database
        if (db.isConnected()) {
            validContact = db.insertContact(name,email,phone,company,message);
        }

        // TODO Redirect to confirmation page if successful
        if (validContact){
            response.sendRedirect("/securia/confimationPage");//Pagina de confimacion

        }else{
            response.sendRedirect("/securia/contact.html");
        }
    }
}
