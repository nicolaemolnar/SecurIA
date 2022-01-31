package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class login_activity extends AppCompatActivity {

    private TextView btnregister,textViewError;
    private Button btnlogin;
    private EditText emailAddress, password;
    String e,p;

    String urlRegister ="http://25.62.36.206:8080/securia/register.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Init

        this.btnlogin = this.findViewById(R.id.btnlogin);
        this.btnregister = this.findViewById(R.id.btnregister);
        this.textViewError = this.findViewById(R.id.textViewError);

        emailAddress = this.findViewById(R.id.inputEmailAddress);
        password = this.findViewById(R.id.inputPassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e = emailAddress.getText().toString();
                p = password.getText().toString();

                String urlLoginServlet = "http://25.62.36.206:8080/securia/login?email=" + e  + "&password=" + p;
                GetXMLTask task = new GetXMLTask();
                task.execute(new String[] { urlLoginServlet });
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Ir a la pagina web para realizar el registro
                Uri link = Uri.parse(urlRegister);
                Intent i = new Intent(Intent.ACTION_VIEW, link);
                startActivity(i);
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
                if(output.getBoolean("successful_login")){
                    Intent main = new Intent(getApplicationContext(),MainActivity.class);
                    Bundle datos= new Bundle();

                    datos.putString("email",emailAddress.getText().toString());
                    main.putExtras(datos);
                    startActivity(main);
                    finish();
                }else{
                    textViewError.setText("Email or Password are incorrect, try again.");
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }
}
