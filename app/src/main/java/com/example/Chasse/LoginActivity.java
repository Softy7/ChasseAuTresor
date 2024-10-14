package com.example.Chasse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Chasse.Model.User;
import com.example.Chasse.Model.UserRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    protected EditText authentification;
    protected EditText password;
    protected Button confirm;
    protected Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);

        this.authentification = findViewById(R.id.authentification);
        this.password = findViewById(R.id.password);
        this.confirm = findViewById(R.id.send);
        this.back = findViewById(R.id.back);
        this.back.setOnClickListener(v -> finish());

        confirm.setOnClickListener(view -> {
            if (!this.authentification.getText().toString().contains("@")) {
                Toast.makeText(this, "Authentification renseignée invalide", Toast.LENGTH_LONG).show();
            } else if (this.password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Mot de passe non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.password.getText().toString().length() < 8) {
                Toast.makeText(this, "Mot de passe trop court", Toast.LENGTH_LONG).show();
            } else {

                String email = this.authentification.getText().toString();
                String password = this.password.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:55556/sae_tresor/api/")
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
                                System.out.println(user);
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            } else {
                                System.out.println("mot de passe ou email pas bon");
                                Toast.makeText(LoginActivity.this, "mot de passe ou email pas bon", Toast.LENGTH_LONG).show();
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
                finish();
            }
        });
    }

    private void saveFile(Context context, String conn) {
        String filename = "connect";

        // Enregistrer le fichier
        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(conn.getBytes());
            fos.close();

            Toast.makeText(this, "Informations de connexion Enregistrées", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors de l'enregistrement des informations : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
