package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        this.back.setOnClickListener(v -> finish());

        this.send.setOnClickListener(v -> {
            if(this.username.getText().toString().isEmpty()) {
                Toast.makeText(this, "Pseudo non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.name.getText().toString().isEmpty()) {
                Toast.makeText(this, "Nom de Famille non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.fName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Prénom non renseigné", Toast.LENGTH_LONG).show();
            }  else if (this.email.getText().toString().isEmpty()) {
                Toast.makeText(this, "Email non renseigné", Toast.LENGTH_LONG).show();
            } else if (!this.email.getText().toString().contains("@")) {
                Toast.makeText(this, "Email renseigné invalide", Toast.LENGTH_LONG).show();
            } else if (this.sexe.getText().toString().isEmpty()) {
                Toast.makeText(this, "Sexe non renseigné", Toast.LENGTH_LONG).show();
            }  else if (this.password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Mot de passe non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.password.getText().toString().length() < 8) {
                Toast.makeText(this, "Mot de passe trop court", Toast.LENGTH_LONG).show();
            } else if (!this.password.getText().toString().equals(this.passwordC.getText().toString())) {
                Toast.makeText(this, "Veuillez confirmer votre mot de passe", Toast.LENGTH_LONG).show();
            }
        });

    }

}