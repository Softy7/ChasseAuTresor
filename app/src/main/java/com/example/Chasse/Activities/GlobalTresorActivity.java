package com.example.Chasse.Activities;

import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Chasse.Model.System.MainSystem;

import java.util.Locale;

public abstract class GlobalTresorActivity extends AppCompatActivity {

    protected TextToSpeech vocal;
    private Bundle speakParams;
    protected View buttonSpeak = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vocal = new TextToSpeech(this,status -> {
            if (status == TextToSpeech.SUCCESS) {
                vocal.setLanguage(Locale.FRENCH);
            }
        });

        speakParams = new Bundle();
        speakParams.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);

        MainSystem mainSystem = new MainSystem();
        boolean isVocalActivate = mainSystem.readUser(this).getSynthese();


        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(400);
                if(isVocalActivate){
                    speak();
                }
            } catch (InterruptedException ignored){

            }
        });
        thread.start();
    }

    protected void addButtonSpeak(View view){
        buttonSpeak = view;
        buttonSpeak.setOnClickListener(v -> {
            speak();
        });
    }

    protected void textToSpeech(String text){
        vocal.speak(text, TextToSpeech.QUEUE_FLUSH, speakParams, null);
    }


    /**
     * Nécessite de mettre une chaîne de vocal.speak(...) afin que le texte soit cité.
     */
    protected void speak(){}
}
