package com.example.Chasse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BeforeGameActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.before_game_activity);
        Button button = findViewById(R.id.before_game_play_button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(BeforeGameActivity.this, GameActivity.class);
            startActivity(intent);
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button button2 = findViewById(R.id.couleurs);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(this, CouleursActivity.class);
            startActivity(intent);
        });

        Button button3 = findViewById(R.id.charade);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(this, CharadeActivity.class);
            startActivity(intent);
        });

        Button boutonQuizz = findViewById(R.id.quizz);
        boutonQuizz.setOnClickListener(v -> {
           Intent intent = new Intent(this, EnigmaActivity.class);
           startActivity(intent);
        });

//        Button button2 = findViewById(R.id.charade);
//        button.setOnClickListener(v -> {
//            Intent intent = new Intent(this, Charade.class);
//            startActivity(intent);
//        });

    }
}
