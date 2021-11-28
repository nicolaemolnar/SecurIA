package com.privatecomms.securia;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;

public class conexionBD {
    Connection conexion=null;

    //Creamos nuestra funcion para Conectarnos a Postgresql
    public  Connection conexionBD(){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://192.168.5.185:5432/dbcrud_postgresql", "postgres", "admin");
            //conexion = DriverManager.getConnection("jdbc:postgresql://192.168.0.15:5432/madan_tumbes", "postgres", "admin");
        }catch (Exception er){
            System.err.println("Error Conexion"+ er.toString());
        }
        return  conexion;
    }

    //Creamos la funcion para Cerrar la Conexion
    protected  void cerrar_conexion(Connection con)throws  Exception{
        con.close();
    }
}
