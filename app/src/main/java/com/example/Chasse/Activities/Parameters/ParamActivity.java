package com.example.Chasse.Activities.Parameters;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.Model.User;
import com.example.Chasse.R;
import com.google.gson.JsonObject;

import java.util.Locale;


public class ParamActivity extends AppCompatActivity {

    protected AppCompatButton modifprofil;

    protected ImageButton backsetting;

    private TextToSpeech voc;

    protected Button disconnect;
    protected Switch btn;
    protected Button remerciements;
    protected MainSystem mainSystem = new MainSystem();

    @SuppressLint({"SetTextI18n", "MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);

        //Affichage nom prenom à la place de "info bdd"
        User user = new MainSystem().readUser(this);
        String nom = user.getFirstName();
        String prenom = user.getLastName();
        TextView nomprenom=findViewById(R.id.nameaffiche);
        nomprenom.setText(nom + " " + prenom);

        String pseudo = user.getPseudo();
        TextView pseudoText = findViewById(R.id.pseudoAffiche);
        pseudoText.setText(pseudo);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        voc = new TextToSpeech(this,status -> {
            if (status == TextToSpeech.SUCCESS) {
                voc.setLanguage(Locale.FRENCH);
            }
        });


        //Boutton modification d'info
        this.modifprofil = findViewById(R.id.modifprofilbtn);
        this.modifprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParamActivity.this, ChangeProfilActivity.class);
                startActivity(intent);
            }
        });

        //boutton retour Main menu
        this.backsetting = findViewById(R.id.backsetting);
        this.backsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.disconnect = findViewById(R.id.btndisconnect);

        this.disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainSystem.unloadUser(ParamActivity.this);
                Toast.makeText(ParamActivity.this, "Vous êtes déconnecté", Toast.LENGTH_LONG).show();

                finish();
            }
        });

        this.remerciements=findViewById(R.id.btnthanks);

        this.remerciements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParamActivity.this, ThanksMain.class);
                startActivity(intent);
            }
        });

        this.btn = findViewById(R.id.btnvoyant);
        this.btn.setChecked(mainSystem.readUser(ParamActivity.this).getSynthese());
        this.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = mainSystem.readUser(ParamActivity.this);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", user.getEmail());
                jsonObject.addProperty("firstName", user.getFirstName());
                jsonObject.addProperty("lastName", user.getLastName());
                jsonObject.addProperty("pseudo", user.getPseudo());
                jsonObject.addProperty("id", user.getId());

                if (btn.isChecked()) {
                    user.setSynthese(true);
                    jsonObject.addProperty("synthese", user.getSynthese());
                    mainSystem.saveUser(ParamActivity.this, jsonObject.toString());
                    Toast.makeText(ParamActivity.this, "Synthèse activée", Toast.LENGTH_SHORT).show();
                    voc.speak("mode non-voyant activée",TextToSpeech.QUEUE_FLUSH,null);
                } else {
                    user.setSynthese(false);
                    jsonObject.addProperty("synthese", user.getSynthese());
                    mainSystem.saveUser(ParamActivity.this, jsonObject.toString());
                    Toast.makeText(ParamActivity.this, "Synthèse désactivée", Toast.LENGTH_SHORT).show();
                    voc.speak("mode non-voyant désactivée",TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
}
