package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.System.MainSystem;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Registractivity extends AppCompatActivity {

    protected MainSystem mainSystem = new MainSystem();
    protected Button back;
    protected Button send;
    protected EditText username;
    protected EditText lastName;
    protected EditText firstName;
    protected EditText sexe;
    protected EditText email;
    protected EditText password;
    protected EditText passwordC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrer_activity);

        if (mainSystem.readUser(Registractivity.this) != null) {
            finish();
        }

        this.back = findViewById(R.id.back);
        this.send = findViewById(R.id.send);

        this.username = findViewById(R.id.username);
        this.lastName = findViewById(R.id.lName);
        this.firstName = findViewById(R.id.fName);
        this.sexe = findViewById(R.id.sexe);
        this.email = findViewById(R.id.email);
        this.password = findViewById(R.id.password);
        this.passwordC = findViewById(R.id.passwordC);

        this.back.setOnClickListener(v -> finish());

        this.send.setOnClickListener(v -> {
            if(this.username.getText().toString().isEmpty()) {
                Toast.makeText(this, "Pseudo non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.lastName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Nom de Famille non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.firstName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Prénom non renseigné", Toast.LENGTH_LONG).show();
            }  else if (this.email.getText().toString().isEmpty()) {
                Toast.makeText(this, "Email non renseigné", Toast.LENGTH_LONG).show();
            } else if (!this.email.getText().toString().contains("@")) {
                Toast.makeText(this, "Email renseigné invalide", Toast.LENGTH_LONG).show();
            } else if (this.sexe.getText().toString().isEmpty()) {
                Toast.makeText(this, "Sexe non renseigné", Toast.LENGTH_LONG).show();
            }  else if (this.password.getText().toString().isEmpty()) {
                Toast.makeText(this, "Mot de passe non renseigné", Toast.LENGTH_LONG).show();
            } else if (this.password.getText().toString().length() < 8) {
                Toast.makeText(this, "Mot de passe trop court", Toast.LENGTH_LONG).show();
            } else if (!this.password.getText().toString().equals(this.passwordC.getText().toString())) {
                Toast.makeText(this, "Veuillez confirmer votre mot de passe", Toast.LENGTH_LONG).show();
            } else {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:55556/sae_tresor/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                //UserRequest userRequest = new UserRequest(firstName.getText().toString(), lastName.getText().toString(),
                        //username.getText().toString(), email.getText().toString(), password.getText().toString());

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", email.getText().toString());
                jsonObject.addProperty("firstName", firstName.getText().toString());
                jsonObject.addProperty("lastName", lastName.getText().toString());
                jsonObject.addProperty("pseudo", username.getText().toString());
                jsonObject.addProperty("password", password.getText().toString());

                Call<Void> call = apiService.createUser(jsonObject);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            System.out.println("Nouveau utilisateur créé avec succès!");
                            Toast.makeText(Registractivity.this, "Compte créé avec succès !", Toast.LENGTH_LONG).show();
                            Toast.makeText(Registractivity.this, "Vous pouvez vous connecter !", Toast.LENGTH_LONG).show();
                            finish();
                        } else if (response.code() == 409){
                            System.out.println("ERREUR 409 !!!!!");
                            try {
                                String errorMessage = response.errorBody().string();
                                System.out.println(errorMessage);
                                Toast.makeText(Registractivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Registractivity.this, "Une erreur inattendue s'est produit, veuillez réessayez", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            System.out.println(response.message() + ": " + response.code());
                            Toast.makeText(Registractivity.this, response.message() + ": " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        System.out.println(throwable.getMessage());
                        Toast.makeText(Registractivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


}