package com.example.living_cost_calculator_app.ui.my_groups;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.adapter.ExpenseAdapter;
import com.example.living_cost_calculator_app.adapter.UserAdapter;
import com.example.living_cost_calculator_app.adapter.UsersShareAdapter;
import com.example.living_cost_calculator_app.models.Expense;
import com.example.living_cost_calculator_app.models.Group;
import com.example.living_cost_calculator_app.models.User;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.RetrofitClient;
import com.example.living_cost_calculator_app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMyGroupActivity extends AppCompatActivity {
    Group group;
    TextView tvNameGroup, tv_creator;
    Button btn_numMembers, btn_add_user_share, btn_leave_group;
    RecyclerView rv_users_share, rv_Expenses;
    APIService apiService;
    User user, user_creator;
    List<User> ls_user_share = new ArrayList<>();
    List<Expense> ls_expense = new ArrayList<>();
    Toolbar toolbar;
    int num_click_numMembers = 1;

    UsersShareAdapter user_share_adapter;
    ExpenseAdapter expense_adapter;

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
        rv_users_share = findViewById(R.id.rv_users_share);
        btn_add_user_share = findViewById(R.id.btn_add_user_share);
        rv_Expenses = findViewById(R.id.rv_Expenses);
        btn_leave_group = findViewById(R.id.btn_leave_group);
    }

    private void event(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rv_users_share.setVisibility(View.GONE);
        btn_add_user_share.setVisibility(View.GONE);
        for (String id : group.getUsers()) {
            user = new User();
            user.setId(id);
            findUserById(() -> {
                ls_user_share.add(user);
                return null;
            });
        }

        btn_numMembers.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(num_click_numMembers % 2 == 0){
                    rv_users_share.setVisibility(View.GONE);
                    btn_add_user_share.setVisibility(View.GONE);
                    btn_numMembers.setBackgroundColor(R.color.black);
                }else {
                    rv_users_share.setVisibility(View.VISIBLE);
                    btn_add_user_share.setVisibility(View.VISIBLE);
                    btn_numMembers.setBackgroundColor(R.color.purple_500);
                    user_share_adapter = new UsersShareAdapter(getApplicationContext(), ls_user_share);
                    rv_users_share.setHasFixedSize(true);
                    rv_users_share.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    rv_users_share.setAdapter(user_share_adapter);
                    user_share_adapter.notifyDataSetChanged();
                }
                num_click_numMembers ++;
            }
        });
        user = SessionManager.getInstance(getApplicationContext()).getUser();
        btn_leave_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailMyGroupActivity.this);
                builder.setTitle("Message").setMessage("Confirm leave group ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeUser(()->{
                                    if(group != null){
                                        finish();
                                    }
                                    return null;
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });

        btn_add_user_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(DetailMyGroupActivity.this);
                input.setHint("Enter Username");

                new AlertDialog.Builder(DetailMyGroupActivity.this)
                        .setTitle("ADD USER")
                        .setView(input)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String userInput = input.getText().toString().trim();
                                user = new User(userInput);
                                findUserByUsername(()->{
                                    if(user.getId() != null){
                                        addUser(()->{

                                            return null;
                                        });
                                    }
                                    return null;
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        tvNameGroup.setText(group.getName());
        btn_numMembers.setText(group.getUsers().size() + " members");
        toolbar.setTitle(group.getName() + " Details");
        user = new User();
        user.setId(group.getCreator());
        findUserById(() -> {
            tv_creator.setText(user.getUsername());
            user_creator = user;
            return null;
        });
        getExpensesByGroupId(()->{
//            Log.d("test", ls_expense.get(0).getName());
            expense_adapter = new ExpenseAdapter(getApplicationContext(), ls_expense);
            rv_Expenses.setHasFixedSize(true);
            rv_Expenses.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            rv_Expenses.setAdapter(expense_adapter);
            expense_adapter.notifyDataSetChanged();
            return null;
        });

    }

    public void removeUser(Callable function){
        apiService = RetrofitClient.getAPIService();
        List<String> ls_user = new ArrayList<>();
        ls_user.add(user.getId());
        Call<Group> call = apiService.removeUser(new Group.group_user(group.getId(), ls_user));
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if(response.isSuccessful()) {
                    group = response.body();
                    try {
                        function.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }

    public void addUser(Callable function){
        apiService = RetrofitClient.getAPIService();
        List<String> ls_user = new ArrayList<>();
        ls_user.add(user.getId());
        Call<Group> call = apiService.addUser(new Group.group_user(group.getId(), ls_user));
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if(response.isSuccessful()) {
                    group = response.body();
                    try {
                        function.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }

    private void getExpensesByGroupId(Callable function){
        apiService = RetrofitClient.getAPIService();
        Call<List<Expense>> call = apiService.getExpensesByGroupId(group.getId());
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if(response.isSuccessful()) {
                    ls_expense = response.body();
                    try {
                        function.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
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