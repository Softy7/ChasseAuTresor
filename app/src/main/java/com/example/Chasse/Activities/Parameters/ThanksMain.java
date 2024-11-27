package com.example.Chasse.Activities.Parameters;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Activities.GlobalTresorActivity;
import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.R;

import java.util.Locale;

public class ThanksMain extends GlobalTresorActivity {

    protected ImageButton backsetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.thanks_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        this.backsetting = findViewById(R.id.backthanks);
        this.backsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void speak(){
        textToSpeech("Concepteur du jeu : Valentin Menu, Nathan Bernard, Yanis Vangalen, Ewan Michel et Charles Parsy");
    }
}
