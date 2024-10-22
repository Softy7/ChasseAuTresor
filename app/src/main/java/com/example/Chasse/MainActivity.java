package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.Chasse.Model.System.MainSystem;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    protected ImageButton createGame;
    protected ImageButton loadGame;
    protected ImageButton params;
    protected ImageView bateau;

    protected MainSystem mainSystem = new MainSystem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mainSystem.readUser(MainActivity.this) == null) {
            Intent intent = new Intent(this, ChoiceConnectActivity.class);
            startActivity(intent);
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        bateau = findViewById(R.id.bateau);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bateau);
        bateau.startAnimation(animation);


        createGame = findViewById(R.id.CreateGame);
        createGame.setOnClickListener(v -> {});

        loadGame = findViewById(R.id.LoadGame);
        loadGame.setOnClickListener(v -> {});

        params = findViewById(R.id.params);
        params.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParamActivity.class);
            startActivity(intent);
        });
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