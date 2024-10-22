package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.System.MainSystem;

public class ChoiceConnectActivity extends AppCompatActivity {

    protected ImageButton connect;
    protected ImageButton registrer;
    protected MainSystem mainSystem = new MainSystem();
    protected ImageView bateau;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_connect_activity);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        bateau = findViewById(R.id.bateau);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bateau);
        bateau.startAnimation(animation);


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

    }


}
