package com.example.Chasse.Activities.LoadGame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Chasse.ApiService;
import com.example.Chasse.Model.SocketManager;
import com.example.Chasse.Model.System.MainSystem;
import com.example.Chasse.R;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadGameActivity extends AppCompatActivity {

    private MainSystem mainSystem = new MainSystem();
    protected ImageButton back, ok;
    private EditText code;
    private Socket socket;
    protected static final String IS_JOINING_ROOM = "isJoiningRoom";
    protected static final String CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_game_layout);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        this.back = findViewById(R.id.back_load_game);
        this.back.setOnClickListener(v -> finish());
        SocketManager.destroyInstance();

        this.code = findViewById(R.id.editTextText2);

        this.ok = findViewById(R.id.ok_load_game);


        this.ok.setOnClickListener(v -> {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(new MainSystem().getAddressNodejsServor())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                try {
                    long codeText = Long.parseLong(this.code.getText().toString());
                    Call<Void> call = apiService.getRoomStatus(codeText);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                Intent intent = new Intent(LoadGameActivity.this, InviteFriendActivity.class);
                                intent.putExtra(IS_JOINING_ROOM, true);
                                Log.e("code", String.valueOf(codeText));
                                intent.putExtra(CODE, codeText);
                                startActivity(intent);
                            } else if (response.code() == 202){
                                Toast.makeText(LoadGameActivity.this, "Le groupe que vous avez rentré est complet", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoadGameActivity.this, "Le groupe n'existe pas", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            Toast.makeText(LoadGameActivity.this, "Erreur : " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (NumberFormatException e){
                    Toast.makeText(LoadGameActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (RuntimeException e) {
                Toast.makeText(LoadGameActivity.this, "Une erreur s'est produit, veillez réassayer", Toast.LENGTH_LONG).show();
                throw new RuntimeException(e);
            }


        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


