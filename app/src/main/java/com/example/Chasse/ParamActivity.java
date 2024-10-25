package com.example.Chasse;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.Model.User;
import com.google.android.material.tabs.TabLayout;


public class ParamActivity extends AppCompatActivity {

    protected AppCompatButton modifprofil;

    protected ImageButton backsetting;

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

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


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
                Intent intent = new Intent(ParamActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
}
