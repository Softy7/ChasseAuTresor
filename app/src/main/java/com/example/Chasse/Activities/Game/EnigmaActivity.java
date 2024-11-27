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

            socket.on("enigma index", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    int index = (int) objects[0];
                    runOnUiThread(() -> {
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
                            boolean allButtonsIsVisible = true;
                            for (ImageButton button : buttons) {
                                if (button.getVisibility() == View.GONE){
                                    allButtonsIsVisible = false;
                                    break;
                                }
                            }
                            // Tous les boutons sont visibles
                            if (allButtonsIsVisible){
                                // Cas 1 où les 2 réponses sont identiques
                                if (yourResponse == responsePlayer2){
                                    checkAnswer(yourResponse);
                                    // Cas 2 où les réponses sont différentes
                                } else {
                                    removeButton();
                                    responsePlayer2 = ' ';
                                    buttonsAction(true);
                                }
                                // Cas où tous les boutons sont pas là
                            } else {
                                if (yourResponse == responsePlayer2){
                                    checkAnswer(yourResponse);
                                } else {
                                    gameFinished(false);
                                }
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

        private void removeButton(){
            Map<Character, Integer> buttonMap = new HashMap<>();
            buttonMap.put('A', 0);
            buttonMap.put('B', 1);
            buttonMap.put('C', 2);
            buttonMap.put('D', 3);

            List<Character> keepResponses = Arrays.asList(yourResponse, responsePlayer2);

            for (Map.Entry<Character, Integer> entry : buttonMap.entrySet()) {
                Character response = entry.getKey();
                int index = entry.getValue();

                if (!keepResponses.contains(response)) {
                    // Vérifier si l'indice est valide avant de masquer les vues
                    if (index >= 0 && index < buttons.size()) {
                        buttons.get(index).setVisibility(View.GONE);
                        texts.get(index).setVisibility(View.GONE);
                    }
                }
            }
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
        }



}




