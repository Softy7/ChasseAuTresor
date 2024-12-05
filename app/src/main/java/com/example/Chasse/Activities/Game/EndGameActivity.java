package com.example.Chasse.Activities.Game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.Chasse.Activities.GlobalTresorActivity;
import com.example.Chasse.Activities.MainActivity;
import com.example.Chasse.R;

public class EndGameActivity extends GlobalTresorActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Intent intent = getIntent();
        int numberGamePlayed = intent.getIntExtra("miniGamesPlayed", 0);
        int numberGameWon = intent.getIntExtra("counterMiniGamesWon", 0);

        TextView textResult = findViewById(R.id.score);
        if (numberGamePlayed > 0){
            textResult.setText("Bravo! Vous avez gagné " + numberGameWon + " / " + numberGamePlayed + " des mini-jeux joués");
        } else{
            textResult.setText("Une erreur s'est produite");
        }



        ImageButton buttonReturnMainMenu = findViewById(R.id.button_retourn_main_menu);
        buttonReturnMainMenu.setOnClickListener(v -> {
            Intent intentMainMenu = new Intent(this, MainActivity.class);
            startActivity(intentMainMenu);
            finish();
        });
    }
}
