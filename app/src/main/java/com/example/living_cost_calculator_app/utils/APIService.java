package com.example.living_cost_calculator_app.utils;

import com.example.living_cost_calculator_app.models.Expense;
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
import retrofit2.http.PUT;
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
    @GET("user/{user_id}")
    Call<User> findUserById(@Path("user_id") String user_id);
    @POST("group")
    Call<Group> create_group(@Body Group group);
    @GET("group/user/{user_id}")
    Call<List<Group>> getGroupsByUserId(@Path("user_id") String user_id);
    @GET("expense/group/{group_id}")
    Call<List<Expense>> getExpensesByGroupId(@Path("group_id") String group_id);
    @PUT("group/remove-user")
    Call<Group> removeUser(@Body Group.group_user group_user);
    @PUT("group/add-user")
    Call<Group> addUser(@Body Group.group_user group_user);
}
