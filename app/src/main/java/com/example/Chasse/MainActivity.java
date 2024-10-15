package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.Chasse.Model.System.MainSystem;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    protected Button createGame;
    protected Button loadGame;

    protected MainSystem mainSystem = new MainSystem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mainSystem.readUser(MainActivity.this) == null) {
            Intent intent = new Intent(this, ChoiceConnectActivity.class);
            startActivity(intent);
        }

        createGame = findViewById(R.id.CreateGame);
        createGame.setOnClickListener(v -> {});

        loadGame = findViewById(R.id.LoadGame);
        loadGame.setOnClickListener(v -> {});
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mainSystem.readUser(MainActivity.this) == null) {
            Intent intent = new Intent(this, ChoiceConnectActivity.class);
            startActivity(intent);
        }
    }
}