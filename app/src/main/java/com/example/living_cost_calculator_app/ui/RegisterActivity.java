package com.example.living_cost_calculator_app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.models.User;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.RetrofitClient;
import com.example.living_cost_calculator_app.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    TextView textLogin;
    APIService apiService;
    Button buttonRegister;
    User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mapping();
        setEvent();
    }

    void mapping(){
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        textLogin = findViewById(R.id.textLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    void setEvent(){
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                registerUser();
            }
        });
    }

    private void registerUser() {
        apiService = RetrofitClient.getAPIService();
        if(inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
            User.register_user registerUser = new User.register_user(inputEmail.getText().toString(), inputName.getText().toString(), inputPassword.getText().toString());
            Call<User> call = apiService.register(registerUser);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("Success").setMessage("You have successfully registered !!")
                                .setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    }).show();
                    } else {
                        String errorMessage = "";
                        try {
                            JsonObject errorJson = new Gson().fromJson(response.errorBody().string(), JsonObject.class);
                            errorMessage = errorJson.get("message").getAsString();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<User> call, Throwable t) {
                    Log.d("Failed to call API", t.getMessage());
                    Toast.makeText(RegisterActivity.this, "Failed to call API", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
            });
        }else{
            Toast.makeText(RegisterActivity.this, "Confirm Password incorrect !!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }
}