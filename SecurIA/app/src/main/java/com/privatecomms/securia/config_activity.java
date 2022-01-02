package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.SwitchCompat;

public class config_activity extends Activity {
    Atributos_Usuario atrib=Atributos_Usuario.getInstance();
    EditText userName,email, password, repeatedPassword;
    Button btnBack,btnExit;

    SwitchCompat notifications,fotos,stream,videosCapture;
    boolean stateSwitch1,stateSwitch2,stateSwitch3,stateSwitch4;

    SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);

        //init de los botones
        this.btnBack = this.findViewById(R.id.btnBack);
        this.btnExit = this.findViewById(R.id.btnExit);

        //inicio de atributos de usuario y posibilidad de cambio de los mismos
        this.userName = this.findViewById(R.id.userName);
        this.email = this.findViewById(R.id.email);
        this.password = this.findViewById(R.id.password);
        this.repeatedPassword = this.findViewById(R.id.repeatedPassword);

        userName.setText(atrib.get_userName().toString());
        email.setText(atrib.get_email().toString());
        password.setText(atrib.get_password().toString());
        repeatedPassword.setText(atrib.get_repeatedPassword().toString());


        //inicio de los botones de switch de las opciones
        preferences = getSharedPreferences("PREFS",0);
        stateSwitch1 = preferences.getBoolean("sendNotifications",false);
        stateSwitch2 = preferences.getBoolean("captureFotos",false);
        stateSwitch3 = preferences.getBoolean("lifeStream",false);
        stateSwitch4 = preferences.getBoolean("captureVideos",false);

        notifications = this.findViewById(R.id.sendNotifications);
        fotos = this.findViewById(R.id.captureFotos);
        stream = this.findViewById(R.id.lifeStream);
        videosCapture = this.findViewById(R.id.captureVideos);

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

        //funciones de activacion o desactivacion de los switches
        sendNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch1 = !stateSwitch1;
                sendNotifications.setChecked(stateSwitch1);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sendNotifications",stateSwitch1);
                editor.apply();
            }
        });

        captureFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch2 = !stateSwitch2;
                captureFotos.setChecked(stateSwitch2);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("captureFotos",stateSwitch2);
                editor.apply();
            }
        });

        lifeStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch3 = !stateSwitch3;
                lifeStream.setChecked(stateSwitch3);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("lifeStream",stateSwitch3);
                editor.apply();
            }
        });

        captureVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch4 = !stateSwitch4;
                captureVideos.setChecked(stateSwitch4);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("captureVideos",stateSwitch4);
                editor.apply();
            }
        });

    }
}
