package com.example.Chasse;

import com.example.Chasse.Model.User;
import com.example.Chasse.Model.UserRequest;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    // API Spring Trésor
    @POST("user/add")
    Call<Void> createUser(@Body JsonObject jsonObject);

    @GET("user/login")
    Call<UserRequest> login(@Query("object") String object, @Query("password") String password);

    @GET("user/find")
    Call<UserRequest> getUserById(@Query("id") long id);

    @PUT("user/update")
    Call<Void> updateUser(@Body JsonObject jsonObject);

    // API Node.js
    @GET("join-room/status")
    Call<Void> getRoomStatus(@Query("id") long id);
}
