package com.privatecomms.securia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private Button btnConfig, btnExit, btnStream;
    //LinearLayout gallery = findViewById(R.id.gallery);
    //LayoutInflater inflater = LayoutInflater.from(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //bundle para recbir los datos del login
        Bundle datos= this.getIntent().getExtras();

        System.out.println(datos);

        //Init de botones
        this.btnConfig = this.findViewById(R.id.btnConfig);
        this.btnExit = this.findViewById(R.id.btnExit);
        this.btnStream = this.findViewById(R.id.btnStream);

        //le paso los datos del usuario del inicio de sesion del login
        String email = datos.getString("email");
        System.out.println(email);


        LinearLayout gallery = findViewById(R.id.gallery);
        LayoutInflater inflater = LayoutInflater.from(this);


        String urlLoginServlet = "http://25.62.36.206:8080/securia/gallery?email="+email+"&device=android";
        MainActivity.GetXMLTask task = new MainActivity.GetXMLTask();
        task.execute(new Object[] { urlLoginServlet,gallery,inflater });



        //funciones de botones y switch de stream
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent config = new Intent(getApplicationContext(), ConfigActivity.class);
                Bundle datosConfig= new Bundle();

                datosConfig.putString("email",email);
                config.putExtras(datosConfig);
                startActivity(config);
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

        btnStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stream = new Intent(getApplicationContext(), StreamActivity.class);
                Bundle datosStream= new Bundle();

                datosStream.putString("email",email);
                stream.putExtras(datosStream);
                startActivity(stream);
            }
        });

    }

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

    private class GetXMLTask extends AsyncTask<Object, Void, Object[]> {

        @Override
        protected Object[] doInBackground(Object... params) { //Leer respuesta del servidor (String del JSON)
            JSONObject output = null;
            String url = String.valueOf(params[0]);
                String json_string = getOutputFromUrl(url);
                try {
                    output = new JSONObject(json_string);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            return new Object[]{output,params[1],params[2]};
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
        protected void onPostExecute(Object[] params) { //Analizar el resultado pagian web y redirigir o mostrar error
            JSONObject output = (JSONObject) params[0];
            LinearLayout gallery = (LinearLayout) params[1];
            LayoutInflater inflater = (LayoutInflater) params[2];
            try {
                String imagenes = output.getString("img_count");
                int n_imagenes = Integer.parseInt(imagenes);

                for (int i = 0; i <= n_imagenes; i++) {
                    String[] imagen = output.getString(String.valueOf(i)).replace("]","").replace("[","").split(",");
                    String base64String = imagen[0];
                    String time = imagen[1];
                    String etiqueta = imagen[2];

                    Bitmap bm = StringToBitMap(base64String);


                    View view = inflater.inflate(R.layout.item, gallery, false);

                    TextView timestamp = view.findViewById(R.id.timestamp);
                    timestamp.setText(time);

                    TextView label = view.findViewById(R.id.label);
                    label.setText(etiqueta);

                    ImageView image = view.findViewById(R.id.image);
                    image.setImageBitmap(bm);

                    gallery.addView(view);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            /**Iterator<String> iter = output.keys();
             while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = output.get(key);
             } catch (JSONException e) {
                // Something went wrong!
             }
             }**/
            }
        }
}