package com.example.Chasse.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Enigma {
    private final String question;
    private final Choice response;
    private Choices choices;

    public Enigma(String question, Choice response, Choices choices) {
        this.question = question;
        this.response = response;
        this.choices = choices;
    }

    public boolean checkResponse(Choice answer) {
        return Objects.equals(answer, response);
    }

    public String getQuestion() {
        return question;
    }

    public Choice getResponse() {
        return response;
    }

    public Choices getChoices() {
        return choices;
    }
}

