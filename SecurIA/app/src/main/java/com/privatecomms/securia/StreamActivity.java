package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class StreamActivity extends Activity {

    private Button btnBack;
    TextView fecha, evento;
    ImageView imageStream;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_activity);

        this.btnBack = this.findViewById(R.id.btnBack);
        this.fecha = this.findViewById(R.id.fecha);
        this.evento = this.findViewById(R.id.evento);
        this.imageStream = this.findViewById(R.id.imageStream);

        Bundle datosStream= this.getIntent().getExtras();
        String email = datosStream.getString("email");


        String urlLoginServlet = "http://25.62.36.206:8080/securia/streaming?email="+ email;
        StreamActivity.GetXMLTask task = new StreamActivity.GetXMLTask();
        task.execute(new String[] { urlLoginServlet });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
//posible añadir imagen a partir de url??


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    private class GetXMLTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) { //Leer respuesta del servidor (String del JSON)
            JSONObject output = null;
            for (String url : urls) {
                String json_string = getOutputFromUrl(url);
                try {
                    output = new JSONObject(json_string);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
            return output;
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

        @Override
        protected void onPostExecute(JSONObject output) { //Analizar el resultado pagian web y redirigir o mostrar error
            try {
                if(output.getBoolean("success")){
                    String base64String = output.getString("stream");
                    evento.setText(output.getString("label"));

                    //conversion de string a imagen
                    /**String base64Image = base64String.split(",")[1];
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    imageStream.setImageBitmap(decodedByte);**/

                    Bitmap bm = StringToBitMap(base64String);
                    imageStream.setImageBitmap(bm);

                }else{
                   // textViewError.setText("Email or Password are incorrect, try again.");
                }
                /*fecha.setText(output.getString("fecha"));
                */

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }
}
