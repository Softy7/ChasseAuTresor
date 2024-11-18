package com.example.Chasse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class CharadeActivity extends AppCompatActivity {

    protected Button test;

    private static final String FILE_NAME = "charades.json";
    String p1,p2,p3,p4;
    TextView testText;

    int random;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charade_activity);
        testText = (TextView) findViewById(R.id.testText);

        try {
            InputStream test = getAssets().open("charades.json");
            int size = test.available();
            byte[] buffer = new byte[size];
            test.read(buffer);
            test.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            int max = jsonArray.length();

            random = new Random().nextInt(max);

            JSONObject jsonObject = jsonArray.getJSONObject(random);
            p1 = jsonObject.getString("A");
            p2 = jsonObject.getString("B");
            p3 = jsonObject.getString("C");
            p4 = jsonObject.getString("D");
            testText.setText(p1+p2+p3+p4);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        /*Test Jeu (pas necessaire pour le jeu final)*/
        this.test = findViewById(R.id.test2);
        this.test.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReponseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("random", random);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}