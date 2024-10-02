package com.example.Chasse;

import android.annotation.SuppressLint;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.Cleaner;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    protected Button button_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.button_start = findViewById(R.id.play);
        this.button_start.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registractivity.class);
            startActivity(intent);
        });

        // Pour tester les requêtes asynchrones
        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        Button button = new Button(this);
        button.setText("Test requete api");
        constraintLayout.addView(button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://reqres.in/api/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);

                Call<String> call = apiService.getData();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String myResponse = response.body();
                            System.out.println(myResponse);
                        } else {
                            System.out.println("Request failed: " + response.code() + " " + response.message() + " " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
        });
    }
}