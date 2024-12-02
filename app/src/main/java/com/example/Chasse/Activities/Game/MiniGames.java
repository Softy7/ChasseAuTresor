package com.example.Chasse.Activities.Game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.example.Chasse.Activities.MainActivity;
import com.example.Chasse.Model.SocketManager;
import io.socket.emitter.Emitter;

public abstract class MiniGames extends Games {

    protected static final String IS_WON = "isWon";
    protected static final String COUNTER_MINI_GAMES_PLAYED = "miniGamesPlayed";
    protected static final String COUNTER_MINI_GAMES_WON = "counterMiniGamesWon";
    protected boolean isTheMainUser;
    private int miniGamesPlayed;
    private int counterMiniGamesWon;
    private Intent intentMainGame;
    private boolean isWonTheGame;
    private boolean isTheGameWillStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        miniGamesPlayed = intent.getIntExtra("miniGamesPlayed", 0) + 1;
        counterMiniGamesWon = intent.getIntExtra("numberOfMiniGamesWon", 0);
        boolean isGameFinished = intent.getBooleanExtra("isTheLastPart", true);
        isTheMainUser = intent.getBooleanExtra("isTheMainUser", false);
        Log.d("Game finished ? ", String.valueOf(isGameFinished));
        Log.d("Main user ?", String.valueOf(isTheMainUser));


        socket.on("game starting", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                socket.off("game starting");
                long userId = Long.parseLong(objects[0].toString());
                boolean isWon = (boolean) objects[1];
                boolean isMainUser = userId == game.getUserId();
                runOnUiThread(() -> {
                    Log.d("is won ???", String.valueOf(isWon));
                    int res = isWon ? 1 : 0;
                    String text = isWon ? "Bravo ! Vous avez gagné ce mini-jeu." : "Oh non, vous avez perdu ce mini-jeu.";
                    Toast.makeText(MiniGames.this,
                                    text + (counterMiniGamesWon + res) + "/" + miniGamesPlayed + " mini-jeux gagné",
                                    Toast.LENGTH_LONG)
                            .show();

                    Log.d("game finished", String.valueOf(isGameFinished));
                    if (!isGameFinished) {
                        intentMainGame = new Intent(MiniGames.this, GameActivity.class);
                        intentMainGame.putExtra(COUNTER_MINI_GAMES_PLAYED, miniGamesPlayed);
                        intentMainGame.putExtra(IS_WON, isWon);
                        intentMainGame.putExtra(IS_THE_MAIN_USER, isMainUser);
                        intentMainGame.putExtra(COUNTER_MINI_GAMES_WON, counterMiniGamesWon + res);
                        isTheGameFinished = false;
                        startActivity(intentMainGame);
                    } else {
                        Intent mainMenuIntent = new Intent(MiniGames.this, MainActivity.class);
                        game.setFinished(true);
                        if (socket != null) {
                            socket.disconnect();
                            socket.close();
                        }
                        SocketManager.destroyInstance();
                        startActivity(mainMenuIntent);
                    }
                    finish();

                });
            }
        });

        socket.on("mini game finished", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                isWonTheGame = (boolean) objects[0];
                Log.d("is won ??", String.valueOf(isTheGameWillStart));
                if (isTheMainUser && !isTheGameWillStart) {
                    isTheGameWillStart = true;
                    Log.d("is won ??", String.valueOf(isWonTheGame));
                    socket.emit("game starts", isWonTheGame);
                }
            }
        });
    }


    public void gameFinished(boolean isWon) {
        socket.emit("mini game finished", isWon);
    }

    protected void onPrePreDestroy(){}

    @Override
    protected void onPreDestroy(){
        onPrePreDestroy();
        socket.off("mini game finished");
    }


}
