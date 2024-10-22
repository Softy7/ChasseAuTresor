package com.example.Chasse;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Model.Game;
import com.example.Chasse.Model.System.MainSystem;

public class InviteFriendActivity extends AppCompatActivity {

    private MainSystem mainSystem = new MainSystem();
    public Game game = new Game();
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.game.addUser(mainSystem.readUser(this));
        this.game.setCode();
    }
}
