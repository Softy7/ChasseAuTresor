package com.example.Chasse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Chasse.Model.System.MainSystem;

public class ChoiceConnectActivity extends AppCompatActivity {

    protected ImageButton connect;
    protected ImageButton registrer;
    protected MainSystem mainSystem = new MainSystem();
    protected ImageView bateau;
    protected ImageView nuage_1;
    protected ImageView nuage_2;
    protected ImageView nuage_3;

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

        nuage_1 = findViewById(R.id.nuage_1);
        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.nuage_1);
        animation1.setInterpolator(new LinearInterpolator());
        nuage_1.startAnimation(animation1);

        nuage_2 = findViewById(R.id.nuage_2);
        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.nuage_2);
        animation2.setInterpolator(new LinearInterpolator());
        nuage_2.startAnimation(animation2);

        nuage_3 = findViewById(R.id.nuage_3);
        Animation animation3 = AnimationUtils.loadAnimation(this,R.anim.nuage_3);
        animation3.setInterpolator(new LinearInterpolator());
        nuage_3.startAnimation(animation3);

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
        ConstraintLayout constraintLayout = findViewById(R.id.constraint_choice_connect_activity);
        Button button = new Button(this);
        button.setText("Jeu");
        constraintLayout.addView(button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(ChoiceConnectActivity.this, BeforeGameActivity.class);
            startActivity(intent);
        });

    }


}
