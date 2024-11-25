package com.example.Chasse.Activities.Game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public abstract class MiniGames extends Games {

    protected static final String IS_WON = "isWon";
    protected static final String COUNTER_MINI_GAMES_PLAYED = "counterMiniGamesPlayed";
    protected static final String COUNTER_MINI_GAMES_WON = "counterMiniGamesWon";
    protected static final String IS_THE_MAIN_USER = "isTheMainUser";
    protected boolean isTheMainUser = false;
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
        isTheMainUser = intent.getBooleanExtra("isTheMainUserNextGame", false);
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
        /*
        TODO: choisir l'utilisateur principal côté serveur
         */
        if (!isGameFinished) {
            isTheGameFinished = false;
            startActivity(intent);
        }
        finish();
    }


}
