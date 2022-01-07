package com.privatecomms.securia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class MainActivity extends AppCompatActivity {


    private Button btnConfig, btnExit;
    SwitchCompat stream;
    boolean stateStream;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //bundle para recbir los datos del login
        Bundle datos = this.getIntent().getExtras();

        //Init de botones
        this.btnConfig = this.findViewById(R.id.btnConfig);
        this.btnExit = this.findViewById(R.id.btnExit);
        //switch de stream
        preferences = getSharedPreferences("PREFS",0);
        stateStream = preferences.getBoolean("stream",false);
        //this.stream = this.findViewById(R.id.stream);

        //le paso los datos del usuario del inicio de sesion del login
        String email = datos.getString("email");
        String password = datos.getString("password");

        /**
        //funciones de botones y switch de stream
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent config = new Intent(getApplicationContext(),config_activity.class);
                config.putExtra("email",email);
                startActivity(config);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateStream = !stateStream;
                stream.setChecked(stateStream);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("stream",stateStream);
                editor.apply();
            }
        });**/

    }

}