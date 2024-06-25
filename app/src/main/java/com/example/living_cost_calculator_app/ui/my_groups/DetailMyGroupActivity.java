package com.example.living_cost_calculator_app.ui.my_groups;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.models.Group;
import com.example.living_cost_calculator_app.models.User;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.RetrofitClient;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMyGroupActivity extends AppCompatActivity {
    Group group;
    TextView tvNameGroup, tv_creator;
    Button btn_numMembers;
    APIService apiService;
    User user, user_creator;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_group);
        group = (Group) getIntent().getSerializableExtra("group_click");
        map();
        event();
    }

    private void map(){
        tvNameGroup = findViewById(R.id.tvNameGroup);
        tv_creator = findViewById(R.id.tv_creator);
        btn_numMembers = findViewById(R.id.btn_numMembers);
        toolbar = findViewById(R.id.toolbar);
    }

    private void event(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        btn_numMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvNameGroup.setText(group.getName());
        btn_numMembers.setText(group.getUsers().size() + " members");
        toolbar.setTitle(group.getName() + " Details");
        user = new User();
        user.setId(group.getCreator());
        findUserById(() -> {
            tv_creator.setText(user.getUsername());
            return null;
        });
    }



    private void findUserById(Callable function ){
        apiService = RetrofitClient.getAPIService();
        Call<User> call = apiService.findUserById(user.getId());
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