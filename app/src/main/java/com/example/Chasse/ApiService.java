package com.example.Chasse;

import com.example.Chasse.Model.User;
import com.example.Chasse.Model.UserRequest;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // Pour le test
    @GET("users")
    Call<String> getData();

    // API Spring Trésor
    @POST("user/add")
    Call<Void> createUser(@Body JsonObject jsonObject);

    @GET("user/login")
    Call<UserRequest> login(@Query("object") String object, @Query("password") String password);
}
