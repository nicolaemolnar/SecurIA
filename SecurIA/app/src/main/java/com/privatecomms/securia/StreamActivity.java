package com.privatecomms.securia;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Timer;

public class StreamActivity extends Activity {

    private Button btnBack;
    TextView fecha;
    ImageView imageStream;

    private boolean encendido= true;

    final Handler handler = new Handler();
    Timer timer = new Timer();

    private StreamActivity StreamActivity;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_activity);

        this.btnBack = this.findViewById(R.id.btnBack);
        this.fecha = this.findViewById(R.id.fecha);
        this.imageStream = this.findViewById(R.id.imageStream);

        encendido= true;

        Bundle datosStream= this.getIntent().getExtras();
        String email = datosStream.getString("email");

        String urlLoginServlet = "http://25.62.36.206:8080/securia/streaming?email="+ email;

        ServerConnectionThread task = new ServerConnectionThread(this, urlLoginServlet);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                encendido=false;
                finish();
            }
        });
    }

    public void cambiaFrame(JSONObject output){
        try {
            if (!output.getString("stream").isEmpty()) {
                String base64String = output.getString("stream");

                fecha.setText(dateToString(LocalTime.now()));

                Bitmap bm = StringToBitMap(base64String);
                imageStream.setImageBitmap(bm);
            } else {
                 imageStream.setImageResource(R.drawable.no_stream);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean getEncendido(){
        return encendido;
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

    public String dateToString(LocalTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm.ss");
        String timeString = date.format(formatter);

        return timeString;
    }

    private class ServerConnectionThread extends Thread{
        private StreamActivity activity;
        private String tag = "ServerConnectionThread";
        private String urlStr = "";

        public ServerConnectionThread(StreamActivity activ, String url)    {
            activity = activ;
            urlStr = url;
            start();
        }

        @Override
        public void run()    {
            String response = "";

            while(activity.getEncendido()) {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();

                    //Get the information from the url
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    response = convertStreamToString(in);
                    Log.d(tag, "get json: " + response);
                    JSONObject output = new JSONObject(response);
                    activity.cambiaFrame(output);
                } catch (Exception e) {

                }
            }
        }

        //Get the input strean and convert into String
        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }
}

