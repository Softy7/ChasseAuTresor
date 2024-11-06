package com.example.Chasse;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoadGameActivity extends AppCompatActivity {

    protected ImageButton back, ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_game_layout);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());

        this.ok = findViewById(R.id.ok);
        this.ok.setOnClickListener(v -> {});
    }
}
