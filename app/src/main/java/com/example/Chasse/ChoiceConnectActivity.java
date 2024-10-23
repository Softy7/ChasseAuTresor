package com.example.Chasse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Chasse.Model.System.MainSystem;

public class ChoiceConnectActivity extends AppCompatActivity {

    protected Button connect;
    protected Button registrer;
    protected MainSystem mainSystem = new MainSystem();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_connect_activity);
        if (mainSystem.readUser(ChoiceConnectActivity.this) != null) {
            finish();
        }

        this.connect = findViewById(R.id.connect);
        this.connect.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceConnectActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        this.registrer = findViewById(R.id.registrering);
        this.registrer.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceConnectActivity.this, Registractivity.class);
            startActivity(intent);
            finish();
        });

        // Pour le test de la connexion wifi
        // Sera effacé à temps
        ConstraintLayout constraintLayout = findViewById(R.id.layoutChoiceConnect);
        Button button = new Button(this);
        button.setText("Jeu");
        constraintLayout.addView(button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceConnectActivity.this, BeforeGameActivity.class);
            startActivity(intent);
        });

    }


}
