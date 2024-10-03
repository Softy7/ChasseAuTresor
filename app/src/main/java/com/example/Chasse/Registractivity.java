package com.example.Chasse;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
            } else {

                User user = new User();
                user.setUserName(this.username.getText().toString());
                user.setLastName(this.name.getText().toString());
                user.setFirstName(this.fName.getText().toString());
                user.setEmail(this.email.getText().toString());
                user.setSexe(this.sexe.getText().toString());
                user.setPassword(this.password.getText().toString());

                String connexion = "{\"username\":'" + user.getUserName() +
                        "', \"name\":'" + user.getLastName() +
                        "', \"fname\":'" + user.getFirstName() +
                        "', \"mail\":'" + user.getEmail() +
                        "', \"sexe\":'" + user.getSexe() +
                        "', \"password\":'" + user.getPassword() + "'}";
                this.saveFile(Registractivity.this, connexion);

            }
        });

    }

    private void saveFile(Context context, String conn) {
        String filename = "connect.json";

        // Enregistrer le fichier
        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(conn.getBytes());
            fos.close();

            Toast.makeText(this, "Informations de connexion Enregistrées", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de l'enregistrement des informations : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}