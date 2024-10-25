package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Chasse.Model.System.MainSystem;
import androidx.appcompat.app.AppCompatActivity;


import com.example.Chasse.Model.User;
import com.example.Chasse.Model.UserRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    protected ImageButton createGame;
    protected ImageButton loadGame;
    protected ImageView bateau;
    protected ImageButton tparams;

    protected User user;

    protected MainSystem mainSystem = new MainSystem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.user = mainSystem.readUser(MainActivity.this);
        if (user == null) {
            Intent intent = new Intent(this, ChoiceConnectActivity.class);
            startActivity(intent);
        } else {
            checkStatusConnexion();
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        bateau = findViewById(R.id.bateau);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bateau);
        bateau.startAnimation(animation);

        this.createGame = findViewById(R.id.CreateGame);
        this.createGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateGameActivity.class);
            startActivity(intent);
        });

        this.loadGame = findViewById(R.id.LoadGame);
        this.loadGame.setOnClickListener(v -> {});

        this.tparams = findViewById(R.id.parametres);
        this.tparams.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ParamActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.user = mainSystem.readUser(MainActivity.this);
        if (user == null) {
            Intent intent = new Intent(this, ChoiceConnectActivity.class);
            startActivity(intent);
        } else {
            checkStatusConnexion();
        }
    }

    private void checkStatusConnexion() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://92.140.29.192:55556/sae_tresor/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        long id = this.user.getId();
        Call<UserRequest> call = apiService.getUserById(id);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {

                if (response.isSuccessful()) {
                    UserRequest user = response.body();
                    if (user == null)  {
                        MainSystem mainSystem = new MainSystem();
                        mainSystem.unloadUser(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, ChoiceConnectActivity.class);
                        startActivity(intent);
                    }
                } else {
                    System.out.println(response.message());
                    Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}