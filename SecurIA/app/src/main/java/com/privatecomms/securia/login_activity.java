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

public class login_activity extends Activity {

    private TextView ViewEmail;
    private TextView ViewPass;
    private Button btnlogin;
    private Button btnregister;
    private EditText inputEmailAddress;
    private EditText inputPassword;

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

            public void onClick(View view) {
                String email = inputEmailAddress.getText().toString();
                String password = inputPassword.getText().toString();




        }
}












}
