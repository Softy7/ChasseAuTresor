package com.example.Chasse.Activities.Game;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.Chasse.Model.Enigma;
import com.example.Chasse.Model.System.MainSystem;

import com.example.Chasse.R;
import io.socket.emitter.Emitter;
import org.json.JSONException;

import java.util.*;

public class EnigmaActivity extends MiniGames{
        protected TextView enigmaText, aText, bText, cText, dText;
        protected MainSystem mainSystem = new MainSystem();
        protected Enigma enigma;
        protected ImageButton buttonA, buttonB, buttonC, buttonD;
        private char yourResponse = ' ';
        private char responsePlayer2 = ' ';
        private List<ImageButton> buttons;
        private List<TextView> texts;
        private int index = -1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_enigma);

            Random random = new Random();
            ArrayList<Enigma> enigmas;
            int enigmaIndex;
            try {
                enigmas = mainSystem.getEnigmas(2, EnigmaActivity.this);
            } catch (JSONException e) {
                Log.d("Erreur", String.valueOf(e));
                throw new RuntimeException(e);
            }

            if (isTheMainUser){
                Log.d("size", String.valueOf(enigmas.size()));
                enigmaIndex = random.nextInt(enigmas.size());
                socket.emit("quizz enigma index", enigmaIndex);
            }


            Thread t = new Thread(() -> {
                // Tant qu'il n'a pas reçu le quizz
                while (index == -1){
                    socket.emit("quizz empty");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {

                    }
                }
                runOnUiThread(() -> getQuestion(enigmas, index));
            });
            t.start();



            socket.on("get quizz question", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    index = (int) objects[0];
                }
            });


            socket.on("enigma index", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    index = (int) objects[0];
                    runOnUiThread(() -> {
                        getQuestion(enigmas, index);
                    });
                }
            });



            socket.on("quizz update", new Emitter.Listener() {

                @Override
                public void call(Object... objects) {
                    Log.d("quizz", "quizz update");
                    runOnUiThread(() -> {
                        // Les 2 joueurs ont répondu
                        if (yourResponse != ' ' && responsePlayer2 == ' '){
                            buttonsAction(false);
                        } else if (yourResponse != ' ') {
                            // Cas 1 où les 2 réponses sont identiques
                            if (yourResponse == responsePlayer2){
                                checkAnswer(yourResponse);
                                // Cas 2 où les réponses sont différentes
                            } else {
                                if (isTheMainUser)
                                    gameFinished(false);
                            }
                        }
                    });
                }
            });

            socket.on("quizz response player", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    Log.d("quizz response", objects[0].toString());
                    responsePlayer2 = (char) objects[0].toString().charAt(0);
                }
            });
        }

        private void getQuestion(ArrayList<Enigma> enigmas, int index){
            enigma = enigmas.get(index);
            enigmaText = findViewById(R.id.enigma);
            enigmaText.setText(enigma.getQuestion());

            buttonA = findViewById(R.id.a);
            buttonA.setOnClickListener(v -> clickResponse('A'));
            buttonB = findViewById(R.id.b);
            buttonB.setOnClickListener(v -> clickResponse('B'));
            buttonC = findViewById(R.id.c);
            buttonC.setOnClickListener(v -> clickResponse('C'));
            buttonD = findViewById(R.id.d);
            buttonD.setOnClickListener(v -> clickResponse('D'));

            aText = findViewById(R.id.aText);
            aText.setText(enigma.getChoices().getAnswer("A"));
            bText = findViewById(R.id.bText);
            bText.setText(enigma.getChoices().getAnswer("B"));
            cText = findViewById(R.id.cText);
            cText.setText(enigma.getChoices().getAnswer("C"));
            dText = findViewById(R.id.dText);
            dText.setText(enigma.getChoices().getAnswer("D"));

            buttons = Arrays.asList(buttonA, buttonB, buttonC, buttonD);
            texts = Arrays.asList(aText, bText, cText, dText);
        }


        public void clickResponse(char letter){
            yourResponse = letter;
            socket.emit("quizz response player", letter);
        }

        public void checkAnswer(char letter) {
            //new Toast(EnigmaActivity.this);
            System.out.println(letter);
            if (this.enigma.checkResponse(this.enigma.getChoices().getChoice(String.valueOf(letter)))) {
                //Toast.makeText(EnigmaActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                Log.d("response", "réponse bonne");
                if (isTheMainUser)
                    gameFinished(true);
            } else {
                //Toast.makeText(EnigmaActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                if (isTheMainUser)
                    gameFinished(false);
                Log.d("response", "réponse mauvaise");
            }
        }

        private void buttonsAction(boolean enable){
            for (ImageButton button : buttons) {
                button.setEnabled(enable);
            }
        }

        @Override
        protected void onPrePreDestroy(){
            socket.off("enigma index");
            socket.off("quizz update");
            socket.off("quizz response player");
            socket.off("get quizz question");
        }



}




