package com.example.living_cost_calculator_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.living_cost_calculator_app.models.User;

public class SessionManager {
    private static final String PREF_NAME = "LoginPreference";
    private static final String KEY_TOKEN = "token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static SessionManager instance;

    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveLoginUser(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public void saveUser(User user){
        editor.putString("USER_NAME", user.getUsername());
        editor.putString("USER_EMAIL", user.getEmail());
        editor.apply();
    }

    public User getUser(){
        String username = sharedPreferences.getString("USER_NAME", "");
        String email = sharedPreferences.getString("USER_EMAIL", "");
        return new User(email, username);
    }

    public String getKeyToken() {
        return sharedPreferences.getString(KEY_TOKEN, "");
    }


    public void clearLoginUser() {
        editor.remove(KEY_TOKEN);
        editor.remove("USER_NAME");
        editor.remove("USER_EMAIL");
        editor.apply();
    }
}