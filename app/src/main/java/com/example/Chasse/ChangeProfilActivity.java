package com.example.Chasse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.Model.User;

public class ChangeProfilActivity extends AppCompatActivity {

    protected ImageButton backsetting;

    @SuppressLint({"SetTextI18n","MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profil);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //Récuperation des informations
        User user = new MainSystem().readUser(this);
        String nom = user.getFirstName();
        String prenom = user.getLastName();
        String pseudo = user.getPseudo();

        //Affichage nom prenom à la place de "info bdd"
        TextView nomprenom=findViewById(R.id.nameaffiche);
        nomprenom.setText(nom + " " + prenom);

        //Affichage nom prenom pseudo dans la modification d'information
        TextView pseudoaffiche=findViewById(R.id.pseudo);
        pseudoaffiche.setText(pseudo);
        TextView nomaffiche=findViewById(R.id.nom);
        nomaffiche.setText(nom);
        TextView prenomaffiche=findViewById(R.id.prenom);
        prenomaffiche.setText(prenom);


        //boutton retour
        this.backsetting = findViewById(R.id.backsetting);
        this.backsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeProfilActivity.this,ParamActivity.class);
                startActivity(intent);
            }
        });

    }
}
