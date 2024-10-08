package com.example.Chasse.Model;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public record UserRequest(String firstName, String lastName, String pseudo, String email, String password) {


    @NonNull
    @NotNull
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.pseudo + " " + this.email;
    }
}
