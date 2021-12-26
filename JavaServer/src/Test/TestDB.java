package Test;

import database.DBConnection;

public class TestDB {
    public static void main(String[] args) {
        DBConnection db = new DBConnection("postgres","123456");
        try{
            db.obtainConnection();
            System.out.println("Connected");
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
