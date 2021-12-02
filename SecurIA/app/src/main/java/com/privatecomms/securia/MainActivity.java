package com.privatecomms.securia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class MainActivity extends AppCompatActivity {

    private Button btnConfig, btnExit;

    //variable de conexion
    private static conexionBD con=new conexionBD();

    //atributos y sincronizacion
    Atributos_Usuario atrib=Atributos_Usuario.getInstance();

    //bundle para rrecbir los datos del login
    Bundle datos = this.getIntent().getExtras();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Init
        this.btnConfig = this.findViewById(R.id.btnConfig);
        this.btnExit = this.findViewById(R.id.btnExit);

        //le paso los datos del usuario del inicio de sesion del login
        String email = datos.getString("email");
        String password = datos.getString("password");

        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Inicio_Configuracion(email,password);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //poner que salga de la app
            }
        });

    }


    //Crearemos la Función para Iniciar Sesion de Postgresql
    public  void Inicio_Configuracion(String usuario, String clave) {
        try {
            String storeProcedureCall = "{CALL pa_logueo_android(?,?,?,?,?,?,?,?)}";
            CallableStatement cStmt = con.conexionBD().prepareCall(storeProcedureCall);

            //Estos dos primeros parametros son los de entrada
            cStmt.setString(1, usuario);
            cStmt.setString(2, clave);
            //Parametros de salida
            cStmt.registerOutParameter(3, Types.VARCHAR);
            cStmt.registerOutParameter(4, Types.VARCHAR);
            cStmt.registerOutParameter(5, Types.VARCHAR);
            cStmt.registerOutParameter(6, Types.VARCHAR);
            cStmt.registerOutParameter(7, Types.VARCHAR);

            cStmt.executeUpdate();

            //variable que recibiremos de postgresql
            String _email=cStmt.getString(3);
            String _userName=cStmt.getString(4);
            String _password=cStmt.getString(5);
            String _repeatedPassword=cStmt.getString(6);
            String msj=cStmt.getString(7);

            if (msj.equals("OK")){
                //si el usuario y contraseña correctos entramos en el menu de la aplicación
                atrib.set_email(_email);
                atrib.set_userName(_userName);
                atrib.set_password(_password);
                atrib.set_repeatedPassword(_repeatedPassword);

                Intent config = new Intent(this, config_activity.class);
                startActivity(config);
            }else{
                Toast.makeText(getApplicationContext(),msj,Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}