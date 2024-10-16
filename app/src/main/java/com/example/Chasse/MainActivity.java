package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    protected Button buttonRegistration;
    protected Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.buttonRegistration = findViewById(R.id.inscription);
        this.buttonRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registractivity.class);
            startActivity(intent);
        });
        this.buttonLogin = findViewById(R.id.login);
        this.buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Pour tester les requêtes asynchrones
        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        Button button = new Button(this);
        button.setText("Jeu");
        constraintLayout.addView(button);
        button.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BeforeGameActivity.class);
                startActivity(intent);
            }
        });
    }
}