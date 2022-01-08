package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_activity);

        this.btnBack = this.findViewById(R.id.btnBack);
        this.fecha = this.findViewById(R.id.fecha);
        this.evento = this.findViewById(R.id.evento);

        Bundle datosStream= this.getIntent().getExtras();
        String email = datosStream.getString("email");


        String urlLoginServlet = "http://25.62.36.206:8080/securia/GetStreamFrameServlet?email="+ email;
        StreamActivity.GetXMLTask task = new StreamActivity.GetXMLTask();
        task.execute(new String[] { urlLoginServlet });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(menu);
                finish();
            }
        });
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
                    output.getString("stream");
                    evento.setText(output.getString("label"));

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
