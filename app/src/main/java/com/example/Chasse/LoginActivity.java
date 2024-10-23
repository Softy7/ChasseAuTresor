package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.Model.UserRequest;
import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    protected EditText authentification;
    protected EditText password;
    protected ImageButton confirm;
    protected ImageButton back;
    protected MainSystem mainSystem = new MainSystem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (mainSystem.readUser(LoginActivity.this) != null) {
            finish();
        }

        this.authentification = findViewById(R.id.authentification);
        this.password = findViewById(R.id.password);
        this.confirm = findViewById(R.id.send);
        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());

        confirm.setOnClickListener(view -> {
            if (!this.authentification.getText().toString().contains("@")) {
                Toast.makeText(this, "Authentification invalide", Toast.LENGTH_LONG).show();
            } else if (this.password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Mot de passe non renseigné", Toast.LENGTH_LONG).show();
            } else {

                String email = this.authentification.getText().toString();
                String password = this.password.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://92.140.29.192:55556/sae_tresor/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                Call<UserRequest> call = apiService.login(email, password);
                call.enqueue(new Callback<UserRequest>() {
                    @Override
                    public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                        if (response.isSuccessful()) {
                            UserRequest user = response.body();
                            if (user != null) {

                                JsonObject jsonObject = new JsonObject();
                                Log.d("email",user.email());
                                jsonObject.addProperty("email", user.email());
                                Log.d("firstName", user.firstName());
                                jsonObject.addProperty("firstName", user.firstName());
                                Log.d("lastName", user.lastName());
                                jsonObject.addProperty("lastName", user.lastName());
                                Log.d("pseudo", user.pseudo());
                                jsonObject.addProperty("pseudo", user.pseudo());
                                Log.d("id", ""+user.userId());
                                jsonObject.addProperty("id", user.userId());

                                if(mainSystem.saveUser(LoginActivity.this, jsonObject.toString())) {
                                    Toast.makeText(LoginActivity.this, "Vous êtes connectés", Toast.LENGTH_LONG).show();
                                    finish();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Email ou Mot de passe Incorrect", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            System.out.println(response.message());
                            Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRequest> call, Throwable throwable) {
                        System.out.println(throwable.getMessage());
                        Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
