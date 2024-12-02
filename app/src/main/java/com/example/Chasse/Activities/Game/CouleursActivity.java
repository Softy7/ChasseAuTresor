package com.example.Chasse.Activities.Game;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.Chasse.R;

import java.util.ArrayList;
import java.util.Random;

public class CouleursActivity extends MiniGames {

        private LinearLayout ll;
        private LinearLayout ll2;

        private ArrayList<Integer> combinaison;
        private ArrayList<Integer> reponse;
        private static final int[] COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

        public CouleursActivity() {
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_couleurs);

                combinaison = new ArrayList<>();
                reponse = new ArrayList<>();
                ll = findViewById(R.id.linearLayout);
                ll2 = findViewById(R.id.linearLayout2);

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
                        gameFinished(reponse.equals(combinaison));

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

}