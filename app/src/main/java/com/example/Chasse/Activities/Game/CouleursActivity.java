package com.example.Chasse.Activities.Game;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.Chasse.Activities.Game.Chat.ChatActivity;
import com.example.Chasse.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.socket.emitter.Emitter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CouleursActivity extends MiniGames {

        private LinearLayout ll;
        private LinearLayout ll2;

        private ArrayList<Integer> combinaison;
        private ArrayList<Integer> reponse;
        private ArrayList<Integer> trueColors;
        private static final Gson GSON = new Gson();
        private static final int[] COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

        public CouleursActivity() {
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_couleurs);

                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                combinaison = new ArrayList<>();
                reponse = new ArrayList<>();
                trueColors = new ArrayList<>();
                ll = findViewById(R.id.linearLayout);
                ll2 = findViewById(R.id.linearLayout2);

                ImageButton openChat = findViewById(R.id.openChat);
                openChat.setOnClickListener(v -> {
                        Intent intentChat = new Intent(CouleursActivity.this, ChatActivity.class);
                        startActivity(intentChat);
                });

                socket.on("receive colors", new Emitter.Listener() {
                        @Override
                        public void call(Object... objects) {
                                Log.d("colors", "je reçois les couleurs " + objects[0].toString());
                                String json = objects[0].toString();
                                Type listType = new TypeToken<List<Integer>>(){}.getType();
                                trueColors = GSON.fromJson(json, listType);
                        }
                });

                if (isTheMainUser){
                        Button red = findViewById(R.id.red);
                        Button green = findViewById(R.id.green);
                        Button blue = findViewById(R.id.blue);
                        Button yellow = findViewById(R.id.yellow);
                        red.setVisibility(View.GONE);
                        green.setVisibility(View.GONE);
                        blue.setVisibility(View.GONE);
                        yellow.setVisibility(View.GONE);
                        findViewById(R.id.textRemoveColor).setVisibility(View.GONE);
                        findViewById(R.id.button_remove_last_color).setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        for (int i = 0; i < 10; i++){
                                Random random = new Random();
                                combinaison.add(COLORS[random.nextInt(COLORS.length)]);
                        }
                        String json = GSON.toJson(combinaison);
                        Thread t = new Thread(() -> {
                                // Attend 0.1s avant d'envoyer les couleurs
                                try {
                                        Thread.sleep(100);
                                        Log.d("colors", combinaison.toString());
                                        socket.emit("mini game color send colors", json);
                                } catch (InterruptedException ignored) {

                                }

                        });
                        t.start();
                } else {
                        for (int i = 0; i < 10; i++){
                                combinaison.add(Color.BLACK);
                        }
                }


                for (int i = 0; i < combinaison.size(); i++) {
                        View v = new View(this);
                        v.setBackgroundColor(combinaison.get(i));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80); // Par exemple, 100x100 pixels
                        params.setMargins(10, 10, 10, 10); // Marge entre les vues
                        v.setLayoutParams(params);

                        ll.addView(v);
                }
                removeLastColor();
        }

        public void red(View v) {
                addColor(Color.RED);
        }

        public void blue(View v) {
                addColor(Color.BLUE);
        }

        public void green(View v) {
                addColor(Color.GREEN);
        }

        public void yellow(View v) {
                addColor(Color.YELLOW);
        }

        public void addColor(int color) {
                View v1 = new View(this);
                v1.setBackgroundColor(color);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80); // Par exemple, 100x100 pixels
                params.setMargins(10, 10, 10, 10); // Marge entre les vues
                v1.setLayoutParams(params);
                ll2.addView(v1);
                reponse.add(color);

                if(reponse.size()==combinaison.size()) {
                        Log.d("is won ?", trueColors.toString() + " " + reponse.toString());
                        Log.d("is won ?", String.valueOf(reponse.equals(combinaison)));
                        gameFinished(trueColors.equals(reponse));

                }
        }

        public void removeLastColor() {
                findViewById(R.id.button_remove_last_color).setOnClickListener(v -> {
                        if (!reponse.isEmpty()) {
                                reponse.remove(reponse.size() - 1);
                                int childCount = ll2.getChildCount();
                                if (childCount > 0) {
                                        ll2.removeViewAt(childCount - 1);
                                }
                        }
                });
        }

        @Override
        protected void onPrePreDestroy(){
                socket.off("receive colors");
        }

}