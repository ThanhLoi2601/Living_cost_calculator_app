package com.example.living_cost_calculator_app.utils;

import com.example.living_cost_calculator_app.models.Group;
import com.example.living_cost_calculator_app.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @POST("user/login")
    Call<User.token> login(@Body User.login_user login_user);
    @POST("user/register")
    Call<User> register(@Body User.register_user register_user);
    @GET("user/profile")
    Call<ResponseBody> getProfile(@Header("Authorization") String token);
    @GET("user/find")
    Call<List<User>> findListUser(@Query("q") String query);
    @GET("user/find-by-username")
    Call<User> findByUsername(@Query("q") String query);
    @POST("group")
    Call<Group> create_group(@Body Group group);
}
