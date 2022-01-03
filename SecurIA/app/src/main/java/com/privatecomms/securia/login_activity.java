package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import cz.msebera.android.httpclient.Header;

public class login_activity extends Activity {

    private TextView btnregister;
    private Button btnlogin;
    private EditText emailAddress, password;
    String e,p;
    RequestParams params;
    AsyncHttpClient client;


    //String urlLoginServlet = "http://25.62.36.206:8080/JavaServer/doLogin";
    String urlRegister ="http://25.62.36.206:8080/securia/register.html";


    //variable de conexion
    private static conexionBD con=new conexionBD();

    //atributos y sincronizacion
    Atributos_Usuario atrib=Atributos_Usuario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Init

        this.btnlogin = this.findViewById(R.id.btnlogin);
        this.btnregister = this.findViewById(R.id.btnregister);

        emailAddress = this.findViewById(R.id.inputEmailAddress);
        password = this.findViewById(R.id.inputPassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e = emailAddress.getText().toString();
                p = password.getText().toString();

                String urlLoginServlet = "http://192.168.1.4:43746/Progetto_TWEB/Login?username=" + e  + "&password=" + p;
                GetXMLTask task = new GetXMLTask();
                task.execute(new String[] { urlLoginServlet });

                /*params = new RequestParams();
                params.put("email", e);
                params.put("password", p);
                client.post(urlLoginServlet, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(login_activity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(login_activity.this, "Somethig went wrong", Toast.LENGTH_SHORT).show();
                    }
                    //Incio_Sesion(emailAddress.getText().toString(),password.getText().toString());
                });*/

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
        protected JSONObject doInBackground(String... urls) {
            JSONObject output = null;
            for (String url : urls) {
                String json_string = getOutputFromUrl(url);
                JSONParser parser = new JSONParser();
                try {
                    output = (JSONObject) parser.parse(json_string);
                }
                catch(ParseException ecc){

                }
            }
            return output;
        }

        private String getOutputFromUrl(String url) {
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
            return output.toString();
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

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(JSONObject output) {
            Intent redirect_normal = new Intent(login_page.this, normal_user.class);
            String type = output.get("type").toString();
            if(type.equals("normal"))
                startActivity(redirect_normal);
            else if(type.equals("NULL"))
                user.setText("LOGIN FAILED");
            else if(type.equals("admin"))
                user.setText("NOT NORMAL");

        }
    }
}





/**
 **/


            /**
            //Crearemos la Función para Iniciar Sesion de Postgresql
            public void Incio_Sesion(String usuario, String clave) {
                try {
                    String storeProcedureCall = "{CALL pa_logueo_android(?,?,?,?,?,?,?,?)}";
                    CallableStatement cStmt = con.conexionBD().prepareCall(storeProcedureCall);

                    //Estos dos primeros parametros son los de entrada
                    cStmt.setString(1, usuario);
                    cStmt.setString(2, clave);

                    //Parametros de salida
                    cStmt.registerOutParameter(3, Types.VARCHAR);
                    cStmt.executeUpdate();
                    String msj = cStmt.getString(3); //variable que recibiremos de postgresql

                    if (msj.equals("OK")) {
                        //si el usuario y contraseña correctos entramos en el menu de la aplicación
                        Bundle extras = new Bundle();
                        extras.putString("email", usuario);
                        extras.putString("password", clave);

                        Intent menu = new Intent(this, MainActivity.class);
                        //Agrega el objeto bundle a el Intne
                        menu.putExtras(extras);
                        //Inicia Activity
                        startActivity(menu);
                    } else {
                        Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }**/

