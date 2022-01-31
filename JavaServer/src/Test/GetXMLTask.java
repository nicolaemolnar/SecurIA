package Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class GetXMLTask extends Thread{
    private String url;
    private boolean cont;
    public GetXMLTask(String url){
        this.url = url;
        this.cont = true;
    }

    public void run(){
        JSONObject output = null;

        while (!this.isInterrupted()) {
            String json_string = getOutputFromUrl(url);
            try {
                output = new JSONObject(json_string);
                System.out.println(output.length());
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            onPostExecute(output);
        }
    }

    private String getOutputFromUrl(String url) { // Recibe la String(JSON) que nos envia el servidor
        StringBuffer output = new StringBuffer("");
        try {
            InputStream stream = getHttpConnection(url);
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(stream));
            String s = "";
            while ((s = buffer.readLine()) != null)
                output.append(s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return String.valueOf(output);
    }

    // Makes HttpURLConnection and returns InputStream
    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            stream = httpConnection.getInputStream();
            //if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

            //}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    public void onPostExecute(JSONObject output){
        if (output.getBoolean("success")) {
            String base64String = output.getString("stream");
            /**evento.setText(output.getString("label"));
             fecha.setText(dateToString(LocalTime.now()));
             System.out.println(fecha);

             Bitmap bm = StringToBitMap(base64String);
             imageStream.setImageBitmap(bm);**/
        } else {
            // textViewError.setText("Email or Password are incorrect, try again.");
        }
        /*fecha.setText(output.getString("fecha"));
         */
    }
}