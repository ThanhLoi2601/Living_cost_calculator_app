package com.example.living_cost_calculator_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.SessionManager;
import com.example.living_cost_calculator_app.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.living_cost_calculator_app.utils.RetrofitClient;

public class LoginActivity extends AppCompatActivity {

    EditText inputEmail;
    EditText inputPassword;
    TextView textRegister;
    Button buttonLogin;
    APIService apiService;
    User.token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        setEvent();
        if (!token.getToken().isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void setEvent() {
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                loginUser();
            }
        });
    }

    void loginUser() {
        if(inputEmail.getText().toString().isEmpty() && inputPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Điền Email và Password !!",Toast.LENGTH_SHORT).show();
        }else if(inputEmail.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Điền Email !!",Toast.LENGTH_SHORT).show();
        }else if(inputPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Điền Password !!",Toast.LENGTH_SHORT).show();
        }else {
            apiService = RetrofitClient.getAPIService();
            Call<User.token> call = apiService.login(new User.login_user(inputEmail.getText().toString(), inputPassword.getText().toString()));
            call.enqueue(new Callback<User.token>() {
                @Override
                public void onResponse(Call<User.token> call, Response<User.token> response) {
                    if (response.isSuccessful()) {
                        token = response.body();
                        SessionManager.getInstance(LoginActivity.this).saveLoginUser(token.getToken());
                        Log.d("infor", token.getToken() );
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "";
                        try {
                            JsonObject errorJson = new Gson().fromJson(response.errorBody().string(), JsonObject.class);
                            errorMessage = errorJson.get("error").getAsString();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<User.token> call, Throwable t) {
                Log.d("Failed to call API", t.getMessage());
            }
        });
        }
    }

    void mapping() {
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textRegister);
        token = new User.token(SessionManager.getInstance(LoginActivity.this).getKeyToken());
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }
}
