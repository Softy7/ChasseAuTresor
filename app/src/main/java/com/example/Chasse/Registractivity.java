package com.example.Chasse;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Registractivity extends AppCompatActivity {

    protected Button back;
    protected Button send;
    protected EditText username;
    protected EditText name;
    protected EditText fName;
    protected EditText sexe;
    protected EditText email;
    protected EditText password;
    protected EditText passwordC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrer_activity);

        this.back = findViewById(R.id.back);
        this.send = findViewById(R.id.send);

        this.username = findViewById(R.id.username);
        this.name = findViewById(R.id.lName);
        this.fName = findViewById(R.id.fName);
        this.sexe = findViewById(R.id.sexe);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.passwordC = findViewById(R.id.passwordC);
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}