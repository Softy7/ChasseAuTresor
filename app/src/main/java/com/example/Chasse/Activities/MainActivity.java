package com.example.Chasse.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.Chasse.Activities.LoadGame.CreateGameActivity;
import com.example.Chasse.Activities.LoadGame.LoadGameActivity;
import com.example.Chasse.Activities.Parameters.ParamActivity;
import com.example.Chasse.Activities.Test.BeforeGameActivity;
import com.example.Chasse.ApiService;
import com.example.Chasse.Model.System.MainSystem;
import androidx.appcompat.app.AppCompatActivity;


import com.example.Chasse.Model.User;
import com.example.Chasse.Model.UserRequest;

import com.example.Chasse.R;
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
    protected ImageView nuage_1;
    protected ImageView nuage_2;
    protected ImageView nuage_3;

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

        nuage_1 = findViewById(R.id.nuage_1);
        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.nuage_1);
        animation1.setInterpolator(new LinearInterpolator());
        nuage_1.startAnimation(animation1);

        nuage_2 = findViewById(R.id.nuage_2);
        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.nuage_2);
        animation2.setInterpolator(new LinearInterpolator());
        nuage_2.startAnimation(animation2);

        nuage_3 = findViewById(R.id.nuage_3);
        Animation animation3 = AnimationUtils.loadAnimation(this,R.anim.nuage_3);
        animation3.setInterpolator(new LinearInterpolator());
        nuage_3.startAnimation(animation3);

        this.createGame = findViewById(R.id.CreateGame);
        this.createGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateGameActivity.class);
            startActivity(intent);
        });

        this.loadGame = findViewById(R.id.LoadGame);
        this.loadGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoadGameActivity.class);
            startActivity(intent);
        });

        this.tparams = findViewById(R.id.parametres);
        this.tparams.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ParamActivity.class);
            startActivity(intent);
        });

        // Pour le test de la connexion wifi
        // Sera effacé à temps
        ConstraintLayout constraintLayout = findViewById(R.id.layoutMain);
        Button button = new Button(this);
        button.setText("Jeu");
        constraintLayout.addView(button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BeforeGameActivity.class);
            startActivity(intent);
        });
        button.setVisibility(View.GONE);
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
                .baseUrl(new MainSystem().getAddressSpringServor() + "sae_tresor/api/")
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
                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}