package com.example.Chasse;

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

public class LoginActivity extends AppCompatActivity {

    private EditText emailText, passwordText;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        this.emailText = findViewById(R.id.email_login);
        this.passwordText = findViewById(R.id.password_login);
        this.login = findViewById(R.id.button_login);

        login.setOnClickListener(view -> {
            String email = this.emailText.getText().toString();
            String password = this.passwordText.getText().toString();

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
                    Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}
