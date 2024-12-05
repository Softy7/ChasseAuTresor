package com.example.Chasse.Model;

import android.content.Intent;
import com.example.Chasse.Activities.Game.Chat.ChatService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final ArrayList<User> users = new ArrayList<>();
    private int code = -1;
    private long userId = -1;
    private static Game instance;
    private long userIdPlayer;
    private String pseudoPlayer2;
    private boolean isFinished = false;
    private int idTheme;
    private Intent gameService;

    public Game(){}

    public static Game getInstance(){
        if(instance == null){
            instance = new Game();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public boolean addUser(User user) {
        if(this.users.contains(user)) {
            return false;
        } else {
            this.users.add(user);
            return true;
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public int getCode() {
        return code;
    }

    public void setCode() {
        this.code = new Random().nextInt(1000000);
    }

    public void setCode(int code){
        this.code = code;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserIdPlayer() {
        return userIdPlayer;
    }

    public void setUserIdPlayer(long userIdPlayer) {
        this.userIdPlayer = userIdPlayer;
    }

    public String getPseudoPlayer2() {
        return pseudoPlayer2;
    }

    public void setPseudoPlayer2(String pseudoPlayer2) {
        this.pseudoPlayer2 = pseudoPlayer2;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(int idTheme) {
        this.idTheme = idTheme;
    }

    public Intent getGameService() {
        return gameService;
    }

    public void setGameService(Intent gameService) {
        this.gameService = gameService;
    }
}
