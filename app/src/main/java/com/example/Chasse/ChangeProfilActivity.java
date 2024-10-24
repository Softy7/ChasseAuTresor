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

        //Affichage nom prenom à la place de "info bdd"
        User user = new MainSystem().readUser(this);
        String nom = user.getFirstName();
        String prenom = user.getLastName();
        String pseudo = user.getPseudo();
        TextView nomprenom=findViewById(R.id.nameaffiche);
        nomprenom.setText(nom + " " + prenom);
        TextView pseudoaffiche=findViewById(R.id.pseudo);
        pseudoaffiche.setText(pseudo);
        TextView nomaffiche=findViewById(R.id.nom);
        nomaffiche.setText(nom);
        TextView prenomaffiche=findViewById(R.id.prenom);
        prenomaffiche.setText(prenom);



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
