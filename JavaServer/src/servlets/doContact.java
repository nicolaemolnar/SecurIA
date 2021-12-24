package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class doContact extends HttpServlet {
    public doContact() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // TODO implement doGet
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // TODO Get parameters

        // TODO Connect to database

        // TODO Insert contact into database

        // TODO Redirect to confirmation page if successful
    }
}
