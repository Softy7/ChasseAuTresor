package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.Enigma;
import com.example.Chasse.Model.System.MainSystem;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;

public class EnigmaActivity extends AppCompatActivity {
    protected TextView enigmaText, aText, bText, cText, dText;
    protected MainSystem mainSystem = new MainSystem();
    protected Enigma enigma;
    protected ImageButton A, B, C, D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigma);

        Random random = new Random();
        ArrayList<Enigma> enigmas;

        try {
            enigmas = mainSystem.getEnigmas(2, EnigmaActivity.this);
        } catch (JSONException e) {
            Log.d("Erreur", String.valueOf(e));
            throw new RuntimeException(e);
        }
        Log.d("size", String.valueOf(enigmas.size()));
        this.enigma = enigmas.get(random.nextInt(enigmas.size()));

        this.enigmaText = findViewById(R.id.enigma);
        this.enigmaText.setText(this.enigma.getQuestion());

        this.A = findViewById(R.id.a);
        this.A.setOnClickListener(v -> {
            new Toast(EnigmaActivity.this);
            if (this.enigma.checkResponse(this.enigma.getChoices().getChoice("A"))) {
                finish();
                Toast.makeText(EnigmaActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                Toast.makeText(EnigmaActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        this.B = findViewById(R.id.b);
        this.B.setOnClickListener(v -> {
            new Toast(EnigmaActivity.this);
            if (this.enigma.checkResponse(this.enigma.getChoices().getChoice("B"))) {
                finish();
                Toast.makeText(EnigmaActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                Toast.makeText(EnigmaActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        this.C = findViewById(R.id.c);
        this.C.setOnClickListener(v -> {
            new Toast(EnigmaActivity.this);
            if (this.enigma.checkResponse(this.enigma.getChoices().getChoice("C"))) {
                finish();
                Toast.makeText(EnigmaActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                Toast.makeText(EnigmaActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        this.D = findViewById(R.id.d);
        this.D.setOnClickListener(v -> {
            new Toast(EnigmaActivity.this);
            if (this.enigma.checkResponse(this.enigma.getChoices().getChoice("D"))) {
                finish();
                Toast.makeText(EnigmaActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                Toast.makeText(EnigmaActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        this.aText = findViewById(R.id.aText);
        this.aText.setText(this.enigma.getChoices().getAnswer("A"));
        this.bText = findViewById(R.id.bText);
        this.bText.setText(this.enigma.getChoices().getAnswer("B"));
        this.cText = findViewById(R.id.cText);
        this.cText.setText(this.enigma.getChoices().getAnswer("C"));
        this.dText = findViewById(R.id.dText);
        this.dText.setText(this.enigma.getChoices().getAnswer("D"));
    }
}

