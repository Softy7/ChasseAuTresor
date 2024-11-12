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
    }

    public boolean propose(Choice answer) {
        return Objects.equals(answer, response);
    }
}

