package com.example.Chasse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.User;

import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    protected EditText authentification;
    protected EditText password;
    protected Button confirm;
    protected Button back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

        this.authentification = findViewById(R.id.authentification);
        this.password = findViewById(R.id.password);

        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());

        this.confirm = findViewById(R.id.send);
        this.confirm.setOnClickListener(v -> {
            if (!this.authentification.getText().toString().contains("@")) {
                Toast.makeText(this, "Authentification renseignée invalide", Toast.LENGTH_LONG).show();
            }  else if (this.password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Mot de passe non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.password.getText().toString().length() < 8) {
                Toast.makeText(this, "Mot de passe trop court", Toast.LENGTH_LONG).show();
            } else {
                /*
                User user = new User();
                user.setUserName(this.authentification.getText().toString());
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
                */
                finish();
            }
        });

    }

    private void saveFile(Context context, String conn) {
        String filename = "connect";

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
}
