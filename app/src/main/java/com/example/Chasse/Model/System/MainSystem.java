package com.example.Chasse.Model.System;

import android.content.Context;
import android.util.Log;

import com.example.Chasse.Model.Choice;
import com.example.Chasse.Model.Choices;
import com.example.Chasse.Model.Enigma;
import com.example.Chasse.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Objects;


public class MainSystem {

    public User user;
    private static final String ADDRESS_SPRING_SERVOR = "http://92.140.29.192:55556/";
    //private static final String ADDRESS_NODEJS_SERVOR = "http://92.140.29.192:55557/";
    private static final String ADDRESS_NODEJS_SERVOR = "http://10.0.2.2:55557/";

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
            boolean synthese = jsonObject.getBoolean("synthese");
            int id = jsonObject.getInt("id");

            User user = new User();
            user.setPseudo(username);
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setEmail(mail);
            user.setId(id);
            user.setSynthese(synthese);

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

    public ArrayList<Enigma> getEnigmas(int themeid,Context context) throws JSONException {
        String jsonString = loadJSONFromAsset(context, "enigmas.json");
        ArrayList<Enigma> enigmas1 = new ArrayList<>();

        if (jsonString != null) {
            JSONArray enigmas = new JSONArray(jsonString);

            for (int i = 0; i < enigmas.length(); i++) {
                JSONObject enigma = enigmas.getJSONObject(i);
                int idTheme = enigma.getInt("idTheme");
                Log.d("Theme", idTheme+"");

                if (themeid == idTheme) {
                    String question = enigma.getString("enigma");
                    JSONObject options = enigma.getJSONObject("options");
                    String answer = enigma.getString("answer");
                    Choices choices = new Choices();
                    Log.d("Question", question);
                    Log.d("Response", options.getString("A"));
                    Choice choiceA = new Choice("A", options.getString("A"));
                    choices.addAnswer(choiceA);
                    Log.d("Add", "Completed");
                    choices.addAnswer(new Choice("B", options.getString("B")));
                    Log.d("Add2", "Completed");
                    choices.addAnswer(new Choice("C", options.getString("C")));
                    Log.d("Add3", "Completed");
                    choices.addAnswer(new Choice("D", options.getString("D")));
                    Log.d("Add4", "Completed");
                    enigmas1.add(new Enigma(question, new Choice(answer, options.getString(answer)), choices));
                    Log.d("State", "Completed");
                }
            }
            return enigmas1;
        }
        return null;
    }

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d("String", json);
            return json;
        } catch (IOException ex) {
            Log.d("Erreur", Objects.requireNonNull(ex.getMessage()));
            return null;
        }
    }

    public String getAddressSpringServor(){
        return ADDRESS_SPRING_SERVOR;
    }

    public String getAddressNodejsServor(){
        return ADDRESS_NODEJS_SERVOR;
    }
}
