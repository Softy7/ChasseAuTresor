package com.example.Chasse.Model;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public record UserRequest(int userId, String firstName, String lastName, String pseudo, String email, String password) {


    @NonNull
    @NotNull
    @Override
    public String toString() {
        return this.userId + " " + this.firstName + " " + this.lastName + " " + this.pseudo + " " + this.email;
    }
}
