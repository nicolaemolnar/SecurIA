package com.privatecomms.securia;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class config_activity extends Activity {
    EditText userName,email, password, repeatedPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);

        this.userName = this.findViewById(R.id.userName);
        this.email = this.findViewById(R.id.email);
        this.password = this.findViewById(R.id.password);
        this.repeatedPassword = this.findViewById(R.id.repeatedPassword);

    }

}
