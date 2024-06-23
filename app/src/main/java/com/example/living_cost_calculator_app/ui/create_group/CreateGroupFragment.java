package com.example.living_cost_calculator_app.ui.create_group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.adapter.UserAdapter;
import com.example.living_cost_calculator_app.databinding.FragmentSlideshowBinding;
import com.example.living_cost_calculator_app.models.Group;
import com.example.living_cost_calculator_app.models.User;
import com.example.living_cost_calculator_app.ui.LoginActivity;
import com.example.living_cost_calculator_app.ui.MainActivity;
import com.example.living_cost_calculator_app.ui.RegisterActivity;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.RetrofitClient;
import com.example.living_cost_calculator_app.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupFragment extends Fragment {
    private View root;

    private FragmentSlideshowBinding binding;
    private Button btn_add_group, btn_add_user, btn_reset_members;
    private EditText txt_group_name, txt_username;
    private RecyclerView rc_find_user, rc_user_added;

    Group group_create;
    List<User> list_find_user = new ArrayList<>();
    List<User> list_add_user = new ArrayList<>();
    User user;
    APIService apiService;
    UserAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        map();
        event();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void map(){
        btn_add_group = root.findViewById(R.id.btn_create_group);
        btn_add_user = root.findViewById(R.id.btnAddToGroup);
        btn_reset_members = root.findViewById(R.id.btn_reset_members);
        txt_group_name = root.findViewById(R.id.inputGroupName);
        txt_username = root.findViewById(R.id.edUsername);
        rc_find_user = (RecyclerView) root.findViewById(R.id.rv_find);
        rc_user_added = (RecyclerView) root.findViewById(R.id.rv_added);
    }

    public void event(){
        user = SessionManager.getInstance(root.getContext()).getUser();
        findUserByUsername();

        apiService = RetrofitClient.getAPIService();
        txt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Call<List<User>> call = apiService.findListUser(s.toString());
                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if(response.isSuccessful()){
                            list_find_user = response.body();
                            if(list_find_user != null && !list_find_user.isEmpty()) {
                                Log.d("logg", list_find_user.get(0).getEmail());
                            }else{
                                Log.d("logg", "rrrrrrrr");
                            }
                            display_rc(list_find_user, rc_find_user, txt_username);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.d("logg", t.getMessage());
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= new User(txt_username.getText().toString());
                findUserByUsername();
            }
        });
        btn_reset_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user_keep = list_add_user.get(0);
                list_add_user.clear();
                list_add_user.add(user_keep);
                display_rc(list_add_user, rc_user_added, null);
            }
        });
        btn_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                builder.setTitle("Message").setMessage("Confirm create group ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<String> list_id_users = new ArrayList<>();
                                for(User u : list_add_user){
                                    list_id_users.add(u.getId());
                                }
                                group_create = new Group(txt_group_name.getText().toString(), list_id_users.get(0), list_id_users);
                                Call<Group> call = apiService.create_group(group_create);
                                call.enqueue(new Callback<Group>() {
                                    @Override
                                    public void onResponse(Call<Group> call, Response<Group> response) {
                                        if(response.isSuccessful()){
                                            group_create = response.body();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                                            builder.setTitle("Success").setMessage("You have successfully created !!")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                        }
                                                    }).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Group> call, Throwable t) {
                                        Log.d("logg", t.getMessage());
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });
    }

    public void display_rc(List<User> list_user, RecyclerView rc, EditText txt_username){
        adapter = new UserAdapter(root.getContext(), list_user, txt_username);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        rc.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void findUserByUsername(){
        apiService = RetrofitClient.getAPIService();
        Call<User> call = apiService.findByUsername(user.getUsername());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    user = response.body();
                    boolean check = false;
                    for( User u : list_add_user){
                        if(u.getUsername().equals(user.getUsername())){
                            check = true;
                            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                            builder.setTitle("Message").setMessage(u.getUsername() + " has already added !!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).show();
                        }
                    }
                    if(!check) {
                        list_add_user.add(user);
                        display_rc(list_add_user, rc_user_added, null);
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