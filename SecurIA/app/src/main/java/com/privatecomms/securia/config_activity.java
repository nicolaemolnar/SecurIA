package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class config_activity extends Activity {
    EditText firstName, surName, password, repeatedPassword, phone, birthDate;
    Button btnBack,btnExit,saveUpdate;
    TextView showemail,textViewError;

    String fir, sur, pass, rpass, pho, brith;

    boolean Photos, getVideo, Stream;
    Switch sendNotifications,captureFotos,lifeStream;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);

        //init de los botones
        this.btnBack = this.findViewById(R.id.btnBack);
        this.btnExit = this.findViewById(R.id.btnExit);
        this.saveUpdate = this.findViewById(R.id.saveUpdate);
        this.textViewError = this.findViewById(R.id.textViewError);

        //inicio de atributos de usuario y posibilidad de cambio de los mismos
        this.firstName = this.findViewById(R.id.firstName);
        this.surName = this.findViewById(R.id.surName);
        this.password = this.findViewById(R.id.password);
        this.repeatedPassword = this.findViewById(R.id.repeatedPassword);
        this.phone = this.findViewById(R.id.phone);
        this.birthDate = this.findViewById(R.id.birthDate);

        this.showemail = this.findViewById(R.id.showemail);


        //inicio de los botones de switch de las opciones
        sendNotifications = this.findViewById(R.id.sendNotifications);
        captureFotos = this.findViewById(R.id.captureFotos);
        lifeStream = this.findViewById(R.id.lifeStream);

        Bundle datosConfig = this.getIntent().getExtras();

        String email = datosConfig.getString("email");

        String urlLoginServlet = "http://25.62.36.206:8080/securia/get_settings?email="+ email +"&device=android";
        config_activity.GetXMLTask task = new config_activity.GetXMLTask();
        task.execute(new String[] { urlLoginServlet });

        showemail.setText(email);

        //funciones de botones
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la pagina web para realizar el registro
                Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(menu);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fir = firstName.getText().toString();
                sur = surName.getText().toString();
                pass = password.getText().toString();
                rpass = repeatedPassword.getText().toString();
                pho = phone.getText().toString();
                brith = birthDate.getText().toString();

                if(fir.isEmpty() || sur.isEmpty()||pass.isEmpty()||rpass.isEmpty()||pho.isEmpty()||brith.isEmpty()){
                    textViewError.setText("Some fields are empty.");
                }
                else{
                    //comprobaciones: ninguno esta vacio, comprobar que las contrsaseñas son iguales, comprobar que la fecha es de tipo fecha

                    String urlSetServlet = "http://25.62.36.206:8080/securia/set_settings?email="+ email  +"&password="+ pass  +"&password_conf="+ rpass +"&firstname="+ fir  +"&surname="+ sur  +"&phone="+ pho  +"&birthdate="+ brith +"&getPhotos="+ Photos  +"&getVideos="+ true  +"&canStream="+ Stream  ;
                    config_activity.GetXMLTask task2 = new config_activity.GetXMLTask();
                    task2.execute(new String[] { urlSetServlet });
                }
            }
        });

        sendNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //poner la parte de las notificaciones
                }
                else{

                }
            }
        });

        captureFotos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Photos=true;
                }
                else{
                    Photos=false;
                }
            }
        });

        lifeStream.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Stream=true;
                }
                else{
                    Stream=false;
                }
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
                firstName.setText(output.getString("firstname"));
                surName.setText(output.getString("surname"));
                password.setText(output.getString("password"));
                repeatedPassword.setText(output.getString("password"));
                phone.setText(output.getString("phone"));
                birthDate.setText(output.getString("birthdate"));

                //sendNotifications.setChecked(output.getBoolean(""));
                captureFotos.setChecked(output.getBoolean("getPhotos"));
                lifeStream.setChecked(output.getBoolean("canStream"));


            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }
}
