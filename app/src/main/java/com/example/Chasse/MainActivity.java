package com.example.Chasse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(readConn() == null) {
            Intent intent = new Intent(this, ChoiceConnectActivity.class);
            startActivity(intent);
        }

    }

    private User readConn() {
        String filename = "connect";

        try {
            InputStream inputStream = openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            reader.close();
            inputStream.close();

            String jsonString = jsonBuilder.toString();
            JSONObject jsonObject = new JSONObject(jsonString);

            String username = jsonObject.getString("username");
            String name = jsonObject.getString("name");
            String fname = jsonObject.getString("fname");
            String mail = jsonObject.getString("mail");
            String sexe = jsonObject.getString("sexe");
            String password = jsonObject.getString("password");

            User user = new User();
            user.setUserName(username);
            user.setLastName(name);
            user.setFirstName(fname);
            user.setEmail(mail);
            user.setSexe(sexe);
            user.setPassword(password);

            return user;
        }
        catch (IOException | JSONException ignored) {
            return null;
        }

    }

}