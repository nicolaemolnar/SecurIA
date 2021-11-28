package com.privatecomms.securia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;

public class login_activity extends Activity {

    private TextView ViewEmail, ViewPass;
    private Button btnlogin, btnregister;
    private EditText inputEmailAddress, inputPassword;

    //variable de conexion
    private static conexionBD con=new conexionBD();

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
    }

    //Crearemos la Función para Iniciar Sesion de Postgresql
    public  void Incio_Sesion(String usuario, String clave){
        try{
            String storeProcedureCall="{CALL pa_logueo_android(?,?,?,?,?,?,?,?)}";
            CallableStatement cStmt=con.conexionBD().prepareCall(storeProcedureCall);
            //Estos dos primeros parametros son los de entrada
            cStmt.setString(1,usuario);
            cStmt.setString(2,clave);


}












}
