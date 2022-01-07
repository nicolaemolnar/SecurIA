package Test;

import org.json.JSONObject;

import java.sql.Timestamp;

public class TestDB {
    public static void main(String[] args) {
       String json_string = "{\"id\":\"1\",\"name\":\"test\",\"date\":\"2019-01-01\",\"price\":\"100\"}";
       JSONObject json = new JSONObject(json_string);
        System.out.println(json.get("date"));

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
    }
}
