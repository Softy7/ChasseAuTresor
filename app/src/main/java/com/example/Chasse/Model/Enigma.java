package com.example.Chasse.Model;

import java.util.Objects;

public class Enigma {
    private final String question;
    private final String response;

    public Enigma(String question, String response) {
        this.question = question;
        this.response = response;
    }

    public boolean propose(String answer) {
        return Objects.equals(answer, response);
    }
}
