package com.example.Chasse.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Choices {
    private final ArrayList<Choice> choices;

    public Choices() {
        this.choices = new ArrayList<>();
    }

    public boolean addAnswer(Choice choice) {
        if(getAnswer(choice.letter()).equals(null)) {
            return false;
        }
        return this.choices.add(choice);
    }

    public String getAnswer(String letter) {
        for (Choice i: choices) {
            if(Objects.equals(i.letter(), letter)) {
                return i.answer();
            }
        }
        return null;
    }
}
