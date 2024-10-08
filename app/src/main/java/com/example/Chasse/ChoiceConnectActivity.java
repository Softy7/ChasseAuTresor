package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChoiceConnectActivity extends AppCompatActivity {

    protected Button connect;
    protected Button registrer;
    protected Button back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_connect_activity);

        this.connect = findViewById(R.id.connect);
        this.connect.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceConnectActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        this.registrer = findViewById(R.id.registrering);
        this.registrer.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceConnectActivity.this, Registractivity.class);
            startActivity(intent);
        });

        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());
    }
}
