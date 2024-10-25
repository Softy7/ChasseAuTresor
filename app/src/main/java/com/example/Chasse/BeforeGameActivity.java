package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
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
    }


}
