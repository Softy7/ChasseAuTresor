package com.example.Chasse;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.Game;
import com.example.Chasse.Model.System.MainSystem;

public class InviteFriendActivity extends AppCompatActivity {

    private MainSystem mainSystem = new MainSystem();
    public Game game = new Game();
    protected TextView code, theme;
    protected ImageButton back, launch, search;
    protected int idTheme;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.add_friend_game);
        this.idTheme = getIntent().getIntExtra("idTheme", 0);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.game.addUser(mainSystem.readUser(this));
        this.game.setCode();

        this.code = findViewById(R.id.code);
        CharSequence join = "Code: "+this.game.getCode();
        this.code.setText(join);

        this.theme = findViewById(R.id.theme);
        this.theme.setText(this.getTheTheme());

        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());

        this.launch = findViewById(R.id.launch);
        this.launch.setOnClickListener(v -> {});

        this.search = findViewById(R.id.search);
        this.search.setOnClickListener(v -> {});

    }

    private String getTheTheme() {
        return switch (this.idTheme) {
            default -> "Les Pirates";
            case 1 -> "Les Alcools";
            case 2 -> "L'IUT";
            case 3 -> "La Nature";
            case 4 -> "Les voitures";
            case 5 -> "L'informatique";
        };
    }
}
