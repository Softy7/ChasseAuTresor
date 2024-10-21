package com.example.Chasse.Model.System;

import android.content.Context;
import android.util.Log;

import com.example.Chasse.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.StandardOpenOption;


public class MainSystem {

    public User user;

    public MainSystem() {}

    public boolean saveUser(Context context, String conn) {
        String filename = "connect";
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(conn.getBytes());
            fos.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public User readUser(Context context) {
        String filename = "connect";

        try {
            Log.d("Test: ","1");
            InputStream inputStream = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                Log.d("Test: ","2");
                jsonBuilder.append(line);
                Log.d("Données retenues: ",line);
            }

            reader.close();
            inputStream.close();

            String jsonString = jsonBuilder.toString();
            JSONObject jsonObject = new JSONObject(jsonString);

            String username = jsonObject.getString("pseudo");
            String lastName = jsonObject.getString("lastName");
            String firstName = jsonObject.getString("firstName");
            String mail = jsonObject.getString("email");
            int id = jsonObject.getInt("id");

            User user = new User();
            user.setPseudo(username);
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setEmail(mail);
            user.setId(id);

            return user;
        }
        catch (IOException | JSONException ignored) {
            return null;
        }

    }

    public boolean unloadUser(Context context) {
        String filename = "connect";
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(StandardOpenOption.TRUNCATE_EXISTING.ordinal());
            fos.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
