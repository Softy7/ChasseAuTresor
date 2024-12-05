package com.example.Chasse.Activities.LoadGame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.example.Chasse.Activities.Game.Chat.ChatService;
import com.example.Chasse.Activities.Game.GameActivity;
import com.example.Chasse.Activities.GlobalTresorActivity;
import com.example.Chasse.Model.Game;
import com.example.Chasse.Model.SocketManager;
import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.R;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InviteFriendActivity extends GlobalTresorActivity {

    private MainSystem mainSystem = new MainSystem();
    public Game game = Game.getInstance();
    protected TextView code, themeText;
    protected ImageButton back, start, search;
    protected int idTheme;
    private Socket socket;
    private TextView otherPlayerPseudo;
    private boolean isJoiningRoom;
    private boolean loadGame = false;
    private static final String IS_THE_MAIN_USER = "isTheMainUser";
    private static final String GAME_ID = "gameId";
    private boolean gameStarting = false;
    private static final String IS_THE_FIRST_GAME = "isTheFirstGame";



    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.add_friend_game);
        this.idTheme = getIntent().getIntExtra("idTheme", 0);

        Intent gameService = new Intent(InviteFriendActivity.this, ChatService.class);
        stopService(gameService);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Intent intent = getIntent();
        isJoiningRoom = intent.getBooleanExtra(LoadGameActivity.IS_JOINING_ROOM, false);

        this.game.setIdTheme(idTheme);
        this.game.addUser(mainSystem.readUser(this));

        this.code = findViewById(R.id.code);


        this.themeText = findViewById(R.id.theme);
        this.themeText.setText(this.getTheTheme());

        this.otherPlayerPseudo = findViewById(R.id.other_player_name);

        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());

        this.start = findViewById(R.id.launch);

        socket = SocketManager.newInstance().getSocket();

        this.start.setOnClickListener(v -> {
            socket.emit("game starts");
        });



        if (!isJoiningRoom) {
            this.game.setCode();
            CharSequence join = "Code: "+this.game.getCode();
            this.code.setText(join);
            start.setEnabled(false);

            emitToSocketCreateNewRoom();

            socket.on("room id exists", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    final String alertMessage = (String) objects[0];
                    runOnUiThread(() -> {
                        Log.d("message", alertMessage);
                        game.setCode();
                        emitToSocketCreateNewRoom();
                    });
                }
            });

            socket.on("new room", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    final String alertMessage = (String) objects[0];
                    Log.d("message", alertMessage);
                }
            });
        } else {
            this.game.setCode((int) intent.getLongExtra("code", 0));
            CharSequence join = "Code: "+ intent.getLongExtra("code", 0);
            this.code.setText(join);
            emitToJoinExistingRoom();
        }


        socket.on("group update", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                String json = objects[0].toString();
                final int theme = (int) objects[1];
                game.setIdTheme(theme);
                Log.d("Theme", String.valueOf(theme));
                runOnUiThread(() -> themeText.setText(getTheTheme(theme)));
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        long idUser = jsonObject.getLong("id_user");
                        String pseudo = jsonObject.getString("pseudo");
                        System.out.println("User ID: " + idUser + ", Pseudo: " + pseudo + " , Id_theme: " + game.getIdTheme());
                        if (idUser != mainSystem.readUser(InviteFriendActivity.this).getId()){
                            // Met à jour sur le thread principal
                            runOnUiThread(() -> {
                                otherPlayerPseudo.setText(pseudo);
                                game.setUserIdPlayer(idUser);
                                game.setPseudoPlayer2(pseudo);
                            });
                        }
                    }
                    runOnUiThread(() -> start.setEnabled(true));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        socket.on("user disconnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                runOnUiThread(() -> {
                    otherPlayerPseudo.setText("");
                    start.setEnabled(false);
                });
            }
        });

        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                String alertMessage = (String) objects[0];
                runOnUiThread(() -> Toast.makeText(InviteFriendActivity.this, alertMessage, Toast.LENGTH_SHORT).show());

            }
        });

        socket.on("game starting", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                //socket.off("game starting");
                if (!gameStarting) {
                    gameStarting = true;
                    runOnUiThread(() -> {
                        int userId = (int) objects[0];
                        loadGame = true;

                        Intent gameService = new Intent(InviteFriendActivity.this, ChatService.class);
                        startService(gameService);
                        game.setGameService(gameService);

                        Intent intent = new Intent(InviteFriendActivity.this, GameActivity.class);
                        //Intent intent = new Intent(InviteFriendActivity.this, EnigmaActivity.class);
                        //Intent intent = new Intent(InviteFriendActivity.this, PuzzleActivity.class);
                        game.setUserId(mainSystem.readUser(InviteFriendActivity.this).getId());
                        intent.putExtra(IS_THE_MAIN_USER, userId == game.getUserId());
                        intent.putExtra("idTheme", idTheme);
                        intent.putExtra(IS_THE_FIRST_GAME, true);
                        startActivity(intent);
                        finish();
                    });
                }
            }
        });

        if (!socket.connected()){
            socket.connect();
        }


    }

    private void emitToSocketCreateNewRoom(){
        socket.emit("create new room",
                mainSystem.readUser(this).getId(),
                mainSystem.readUser(this).getPseudo(),
                this.game.getCode(),
                this.game.getIdTheme()
        );
    }

    private void emitToJoinExistingRoom(){
        socket.emit("join existing room",
                mainSystem.readUser(this).getId(),
                mainSystem.readUser(this).getPseudo(),
                this.game.getCode()
        );
    }

    private String getThemeName(int numero){
        return switch (numero) {
            case 1 -> "Les Alcools";
            case 2 -> "L'IUT";
            case 3 -> "La Nature";
            case 4 -> "Les voitures";
            case 5 -> "L'informatique";
            default -> "Les Pirates";
        };
    }

    private String getTheTheme() {
        return getThemeName(idTheme);
    }

    private String getTheTheme(int themeId) {
        return getThemeName(themeId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.off("join existing room");
        socket.off("create new room");
        socket.off("group update");
        socket.off("new room");
        if (!loadGame){
            socket.disconnect();
            socket.off();
            SocketManager.destroyInstance();
        }
    }
}
