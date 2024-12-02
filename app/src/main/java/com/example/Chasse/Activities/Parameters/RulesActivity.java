package com.example.Chasse.Activities.Parameters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Activities.GlobalTresorActivity;
import com.example.Chasse.R;

public class RulesActivity extends GlobalTresorActivity {

    protected ImageButton backmenu;

    protected String test;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        this.backmenu = findViewById(R.id.backrules);
        this.backmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView y = findViewById(R.id.regle);
        test = y.getText().toString();
    }

    @Override
    protected void speak(){
        textToSpeech("regle du jeux :" + test);
    }
}