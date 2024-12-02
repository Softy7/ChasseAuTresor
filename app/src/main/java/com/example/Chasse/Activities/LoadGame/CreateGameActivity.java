package com.example.Chasse.Activities.LoadGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Activities.GlobalTresorActivity;
import com.example.Chasse.R;

public class CreateGameActivity extends GlobalTresorActivity {

    protected ImageButton pirate, alcohool, IUT, nature, car, informatic, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game_activity);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.pirate = findViewById(R.id.pirate);
        this.pirate.setOnClickListener(v -> startactivity(0));

        this.alcohool = findViewById(R.id.alcohool);
        this.alcohool.setOnClickListener(v -> startactivity(1));

        this.IUT = findViewById(R.id.IUT);
        this.IUT.setOnClickListener(v -> startactivity(2));

        this.nature = findViewById(R.id.nature);
        this.nature.setOnClickListener(v -> startactivity(3));

        this.car = findViewById(R.id.car);
        this.car.setOnClickListener(v -> startactivity(4));

        this.informatic = findViewById(R.id.informatic);
        this.informatic.setOnClickListener(v -> startactivity(5));

        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());
    }

    private void startactivity(int theme) {
        Intent intent = new Intent(CreateGameActivity.this, InviteFriendActivity.class);
        intent.putExtra("idTheme", theme);
        startActivity(intent);
    }

    @Override
    protected void speak(){
        textToSpeech("choisir un thème parmi : Pirate, Alcool, IUT, Nature, Voitures, Informatique");
    }
}
