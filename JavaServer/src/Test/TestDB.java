package Test;

import database.DBConnection;
import logic.Logic;

import java.sql.Date;

public class TestDB {
    public static void main(String[] args) {
        DBConnection db = new DBConnection("postgres","123456");
        try{
            db.obtainConnection();
            System.out.println("Connected");

            String date_str = Logic.formatDate("01-12-2001");
            Date date1 = Date.valueOf(date_str);

            System.out.println(date1);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
