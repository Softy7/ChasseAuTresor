package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public abstract class MiniGames extends Games {

    protected static final String IS_WON = "isWon";
    protected static final String COUNTER_MINI_GAMES_PLAYED = "counterMiniGamesPlayed";
    protected static final String COUNTER_MINI_GAMES_WON = "counterMiniGamesWon";
    private boolean isGameFinished;
    private int miniGamesPlayed;
    private int counterMiniGamesWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        miniGamesPlayed = intent.getIntExtra("miniGamesPlayed", 0) + 1;
        counterMiniGamesWon = intent.getIntExtra("numberOfMiniGamesWon", 0);
        isGameFinished = intent.getBooleanExtra("isTheLastPart", true);
        Log.d("Game finished ? ", String.valueOf(isGameFinished));
    }


    public void gameFinished(boolean isWon) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(COUNTER_MINI_GAMES_PLAYED, miniGamesPlayed);
        intent.putExtra(IS_WON, isWon);
        int res = isWon ? 1 : 0;
        String text = isWon ? "Bravo ! Vous avez gagné ce mini-jeu." : "Oh non, vous avez perdu ce mini-jeu.";
        intent.putExtra(COUNTER_MINI_GAMES_WON, counterMiniGamesWon + res);
        Toast.makeText(MiniGames.this,
                        text + (counterMiniGamesWon + res) + "/" + miniGamesPlayed + " mini-jeux gagné",
                        Toast.LENGTH_LONG)
                .show();
        if (!isGameFinished)
            startActivity(intent);
        finish();
    }


}
