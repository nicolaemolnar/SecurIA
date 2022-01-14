package com.privatecomms.securia;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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
import java.util.Calendar;
import java.util.Date;


public class ConfigActivity extends Activity {
    EditText firstname, surname, password, repeatedPassword, phone, birthDate;
    Button btnBack,btnExit,saveUpdate;
    TextView showemail,textViewError;

    String fir, sur, pass, rpass, pho, brith;

    boolean Photos, Notifications, Stream;
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
        this.firstname = this.findViewById(R.id.firstName);
        this.surname = this.findViewById(R.id.surName);
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
        ConfigActivity.GetXMLTask task = new ConfigActivity.GetXMLTask("get");
        task.execute(new String[] { urlLoginServlet });

        showemail.setText(email);

        //funciones de botones
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la pagina web para realizar el registro
                finish();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);        // Specify any activity here e.g. home or splash or login etc
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fir = firstname.getText().toString();
                sur = surname.getText().toString();
                pass = password.getText().toString();
                rpass = repeatedPassword.getText().toString();
                pho = phone.getText().toString();
                brith = birthDate.getText().toString();

                try {
                    if (fir.isEmpty() || sur.isEmpty() || pass.isEmpty() || rpass.isEmpty() || pho.isEmpty() || brith.isEmpty()) {
                        textViewError.setText("Some field is empty.");
                    } else if (!pass.equals(rpass)) {
                        textViewError.setText("The password are different.");
                    } else if (!fechaValida(brith)) {
                        textViewError.setText("Date is invalid");
                    } else {
                        String urlSetServlet = "http://25.62.36.206:8080/securia/set_settings?email=" + email + "&password=" + pass + "&password_conf=" + rpass
                                + "&firstname=" + fir + "&surname=" + sur + "&phone=" + pho + "&birthdate=" + brith
                                + "&getPhotos=" + Photos + "&canStream=" + Stream + "&sendNotifications="+ Notifications;
                        ConfigActivity.GetXMLTask task2 = new ConfigActivity.GetXMLTask("set");
                        task2.execute(new String[]{urlSetServlet});
                        textViewError.setText("");
                    }
                }catch (Exception e){

                }
            }
        });

        sendNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Notifications=true;
                }
                else{
                    Notifications=false;
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

    public static Date ParseFecha(String fecha)
    {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        }
        catch (ParseException ex)
        {
            System.out.println(ex);
        }
        return fechaDate;
    }

    public static boolean fechaValida(String fecha) throws ParseException {

        Date actual = Calendar.getInstance().getTime();
        System.out.println(actual);

        if(actual.before(ParseFecha(fecha))){
            return false; //Our date is invalid
        }else{
            return true;
        }
    }



    private class GetXMLTask extends AsyncTask<String, Void, JSONObject> {

        private String type;
        public GetXMLTask(String type){
            this.type = type;
        }

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

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(JSONObject output) { //Analizar el resultado pagian web y redirigir o mostrar error
            try {
                if(type.equals("get")) {
                    firstname.setText(output.getString("firstname"));
                    surname.setText(output.getString("surname"));
                    password.setText(output.getString("password"));
                    repeatedPassword.setText(output.getString("password"));
                    phone.setText(output.getString("phone"));
                    birthDate.setText(output.getString("birthdate"));

                    sendNotifications.setChecked(output.getBoolean("sendNotifications"));
                    captureFotos.setChecked(output.getBoolean("getPhotos"));
                    lifeStream.setChecked(output.getBoolean("canStream"));
                }else if(type.equals("set")){
                    if(!output.getBoolean("success")){
                        textViewError.setText("An error occurred while submitting changes to the database");
                    }
                }

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
    }
}
