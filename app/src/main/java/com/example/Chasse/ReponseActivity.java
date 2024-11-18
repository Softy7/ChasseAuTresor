package com.example.Chasse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ReponseActivity extends AppCompatActivity {

    protected ImageButton valider;

    private static final String FILE_NAME = "charades.json";
    String answer;
    int random;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reponse_activity);
        valider = findViewById(R.id.valider);

        try {
            InputStream test = getAssets().open("charades.json");
            int size = test.available();
            byte[] buffer = new byte[size];
            test.read(buffer);
            test.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);

            Bundle bundle = getIntent().getExtras();
            random = bundle.getInt("random");

            JSONObject jsonObject = jsonArray.getJSONObject(random);
            answer = jsonObject.getString("answer");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        /*Test reponse du joueur*/
        valider.setOnClickListener(v -> {
            EditText reponse = findViewById(R.id.rep);
            if(answer.equalsIgnoreCase(reponse.getText().toString())) {
                this.finish();
            }
            reponse.setText("");
        });
    }
}