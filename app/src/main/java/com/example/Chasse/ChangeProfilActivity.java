package com.example.Chasse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.Model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeProfilActivity extends AppCompatActivity {

    protected ImageButton backsetting;

    protected Button modifprofil;

    private TextView pseudoText;
    private TextView nomText;
    private TextView prenomText;
    private MainSystem mainSystem = new MainSystem();

    @SuppressLint({"SetTextI18n","MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profil);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        //Récuperation des informations
        User user = mainSystem.readUser(this);
        long id = user.getId();
        String prenom = user.getFirstName();
        String nom = user.getLastName();
        String pseudo = user.getPseudo();

        //Affichage nom prenom à la place de "info bdd"
        TextView nomprenom=findViewById(R.id.nameaffiche);
        nomprenom.setText(nom + " " + prenom);

        //Affichage nom prenom pseudo dans la modification d'information
        TextView pseudoaffiche=findViewById(R.id.pseudo);
        pseudoaffiche.setText(pseudo);
        TextView nomaffiche=findViewById(R.id.nom);
        nomaffiche.setText(nom);
        TextView prenomaffiche=findViewById(R.id.prenom);
        prenomaffiche.setText(prenom);


        //boutton retour
        this.backsetting = findViewById(R.id.backsetting);
        this.backsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        this.modifprofil = findViewById(R.id.validemodif);

        this.modifprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://92.140.29.192:55556/sae_tresor/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("userId",id);
                jsonObject.addProperty("pseudo",pseudoaffiche.getText().toString());
                jsonObject.addProperty("lastName", nomaffiche.getText().toString());
                jsonObject.addProperty("firstName", prenomaffiche.getText().toString());

                Call<Void> call = apiService.updateUser(jsonObject);


                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            System.out.println("Modification utilisateur effectué avec succès!");

                            user.setPseudo(pseudoaffiche.getText().toString());
                            user.setLastName(nomaffiche.getText().toString());
                            user.setFirstName(prenomaffiche.getText().toString());

                            JsonObject jsonObject1 = new JsonObject();
                            jsonObject1.addProperty("email", user.getEmail());
                            jsonObject1.addProperty("firstName", user.getFirstName());
                            jsonObject1.addProperty("lastName", user.getLastName());
                            jsonObject1.addProperty("pseudo", user.getPseudo());
                            jsonObject1.addProperty("id", user.getId());

                            mainSystem.unloadUser(ChangeProfilActivity.this);
                            mainSystem.saveUser(ChangeProfilActivity.this, jsonObject1.toString());

                            Toast.makeText(ChangeProfilActivity.this, "Modification effectué avec succès !", Toast.LENGTH_LONG).show();
                            // modification bdd local
                            finish();
                        } else if (response.code() == 409) {
                            System.out.println("ERREUR 409 !!!!!");
                            try {
                                String errorMessage = response.errorBody().string();
                                System.out.println(errorMessage);
                                Toast.makeText(ChangeProfilActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ChangeProfilActivity.this, "Une erreur inattendue s'est produit, veuillez réessayez", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        System.out.println(throwable.getMessage());
                        Toast.makeText(ChangeProfilActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
