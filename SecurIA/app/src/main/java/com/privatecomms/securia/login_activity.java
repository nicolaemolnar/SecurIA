package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class login_activity extends Activity {

    private TextView ViewEmail, ViewPass;
    private Button btnlogin, btnregister;
    private EditText inputEmailAddress, inputPassword;
    String urlWeb = "https://SecurIA.com";

    //variable de conexion
    private static conexionBD con=new conexionBD();

    //atributos y sincronizacion
    Atributos_Usuario atrib=Atributos_Usuario.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //Init
        this.ViewEmail = this.findViewById(R.id.ViewEmail);
        this.ViewPass = this.findViewById(R.id.ViewPass);

        this.btnlogin = this.findViewById(R.id.btnlogin);
        this.btnregister = this.findViewById(R.id.btnregister);

        this.inputEmailAddress = this.findViewById(R.id.inputEmailAddress);
        this.inputPassword = this.findViewById(R.id.inputPassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Incio_Sesion(inputEmailAddress.getText().toString(),inputPassword.getText().toString());
            }
        });


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la pagina web para realizar el registro
                Uri link = Uri.parse(urlWeb);
                Intent i = new Intent(Intent.ACTION_VIEW,link);
            }
        });
    }

    //Crearemos la Función para Iniciar Sesion de Postgresql
    public  void Incio_Sesion(String usuario, String clave) {
        try {
            String storeProcedureCall = "{CALL pa_logueo_android(?,?,?,?,?,?,?,?)}";
            CallableStatement cStmt = con.conexionBD().prepareCall(storeProcedureCall);

            //Estos dos primeros parametros son los de entrada
            cStmt.setString(1, usuario);
            cStmt.setString(2, clave);

            //Parametros de salida
            cStmt.registerOutParameter(3, Types.VARCHAR);
            cStmt.executeUpdate();
            String msj=cStmt.getString(3); //variable que recibiremos de postgresql

        if (msj.equals("OK")){
            //si el usuario y contraseña correctos entramos en el menu de la aplicación
            Bundle extras = new Bundle();
            extras.putString("email",usuario);
            extras.putString("password",clave);

            Intent menu = new Intent(this, MainActivity.class);
            //Agrega el objeto bundle a el Intne
            menu.putExtras(extras);
            //Inicia Activity
            startActivity(menu);
        }else{
            Toast.makeText(getApplicationContext(),msj,Toast.LENGTH_SHORT).show();
        }
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
}
}
