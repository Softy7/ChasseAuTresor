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

import com.google.android.material.bottomsheet.BottomSheetDialog;

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
                        System.out.println("Request failed onFailure: " + throwable.getMessage());
                        throwable.getCause();
                        System.out.println(throwable.getLocalizedMessage());
                        System.out.println("Message : " + throwable.getMessage());
                    }
                });
            }
        });
    }
}