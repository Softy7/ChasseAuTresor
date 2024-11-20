package com.example.Chasse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class CouleursActivity extends AppCompatActivity {

        private LinearLayout ll;
        private LinearLayout ll2;

        private ArrayList<Integer> combinaison;
        private ArrayList<Integer> reponse;

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

                combinaison.add(Color.RED);
                combinaison.add(Color.GREEN);
                combinaison.add(Color.BLUE);
                combinaison.add(Color.BLUE);
                combinaison.add(Color.GREEN);
                combinaison.add(Color.BLUE);
                combinaison.add(Color.RED);
                combinaison.add(Color.RED);

                for (int i = 0; i < combinaison.size(); i++) {
                        View v = new View(this);
                        v.setBackgroundColor(combinaison.get(i));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100); // Par exemple, 100x100 pixels
                        params.setMargins(10, 10, 10, 10); // Marge entre les vues
                        v.setLayoutParams(params);

                        ll.addView(v);
                }
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

        public void addColor(int color) {
                View v1 = new View(this);
                v1.setBackgroundColor(color);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100); // Par exemple, 100x100 pixels
                params.setMargins(10, 10, 10, 10); // Marge entre les vues
                v1.setLayoutParams(params);
                ll2.addView(v1);
                reponse.add(color);

                if(reponse.size()==combinaison.size()) {
                        if(reponse.equals(combinaison)) {
                                this.finish();
                        }
                        ll2.removeAllViews();
                        reponse.clear();
                }
        }
}