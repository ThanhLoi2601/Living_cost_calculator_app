package com.example.living_cost_calculator_app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.models.User;
import com.example.living_cost_calculator_app.ui.my_groups.MyGroupsFragment;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.RetrofitClient;
import com.example.living_cost_calculator_app.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.living_cost_calculator_app.databinding.ActivityMainBinding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Callable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView txtEmail, txtUsername;
    APIService apiService;
    User user = null;
    public FragmentManager fragMan = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        map();
        event();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
            logout();
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void navigateToMyGroups() {
        MyGroupsFragment myGroupsFragment = new MyGroupsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("fragMan", (Serializable) fragMan);
        myGroupsFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main, myGroupsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Log out").setMessage("Confirm log out?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SessionManager.getInstance(MainActivity.this).clearLoginUser();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    private void map(){
        View headerView = binding.navView.getHeaderView(0);
        txtEmail = headerView.findViewById(R.id.txtEmail);
        txtUsername = headerView.findViewById(R.id.txtUsername);
    }

    private void event(){
        user = SessionManager.getInstance(MainActivity.this).getUser();
        if(user.getEmail().isEmpty()) {
            apiService = RetrofitClient.getAPIService();
            String token = "Bearer " + SessionManager.getInstance(this).getKeyToken();
            Call<ResponseBody> call = apiService.getProfile(token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                            Log.d("log", jsonObject.get("email").getAsString());
                            user = new User(jsonObject.get("email").getAsString(), jsonObject.get("username").getAsString());
                            txtEmail.setText(user.getEmail());
                            txtUsername.setText(user.getUsername());
                            SessionManager.getInstance(MainActivity.this).saveUser(user);
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error: Response not successful", Toast.LENGTH_SHORT).show();
                    }
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            txtEmail.setText(user.getEmail());
            txtUsername.setText(user.getUsername());
            findUserByUsername(()->{
                SessionManager.getInstance(this).saveUser(user);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                return null;
            });
        }
    }

    public void findUserByUsername(Callable function){
        apiService = RetrofitClient.getAPIService();
        Call<User> call = apiService.findByUsername(user.getUsername());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    user = response.body();
                    try {
                        function.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }
}