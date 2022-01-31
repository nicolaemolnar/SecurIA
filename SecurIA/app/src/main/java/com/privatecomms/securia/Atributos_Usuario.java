package com.privatecomms.securia;

public class Atributos_Usuario {
    private  static Atributos_Usuario instance;

    //AQUI DECLARAREMOS NUESTRAS VARIABLES GLOBALES PARA ALMACENDAR LOS DATOS DE LA FUNCION DE INSIO DE SESION Y LUEGO MOSTRARLOS EN EL
    //ACTIVITY MENU

    private  static  String _email="";
    private  static  String _userName="";
    private  static  String _password="";
    private  static  String _repeatedPassword="";

    public  String get_email() {
        return _email;
    }
    public  void set_email(String _email) {
        Atributos_Usuario._email = _email;
    }

    public  String get_userName() {
        return _userName;
    }
    public  void set_userName(String _userName) {
        Atributos_Usuario._userName = _userName;
    }

    public  String get_password() {
        return _password;
    }
    public  void set_password(String _password) {
        Atributos_Usuario._password = _password;
    }

    public  String get_repeatedPassword() {
        return _repeatedPassword;
    }

    public  void set_repeatedPassword(String _repeatedPassword) {
        Atributos_Usuario._repeatedPassword = _repeatedPassword;
    }

    public static synchronized Atributos_Usuario getInstance() {
        if (instance == null) {
            instance = new Atributos_Usuario();
        }
        return instance;
    }

}
