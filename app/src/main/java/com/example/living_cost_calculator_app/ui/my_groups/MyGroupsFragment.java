package com.example.living_cost_calculator_app.ui.my_groups;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.adapter.MyGroupAdapter;
import com.example.living_cost_calculator_app.adapter.UserAdapter;
import com.example.living_cost_calculator_app.databinding.FragmentGalleryBinding;
import com.example.living_cost_calculator_app.models.Group;
import com.example.living_cost_calculator_app.models.User;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.RetrofitClient;
import com.example.living_cost_calculator_app.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyGroupsFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private View root;
    private RecyclerView rc_my_groups;

    APIService apiService;
    User user;
    List<Group> ls_groups;
    MyGroupAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
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

    private void event(){
        user = SessionManager.getInstance(root.getContext()).getUser();
        findUserByUsername();
    }

    @Override
    public void onResume() {
        super.onResume();
        findListGroupsByUserId();
    }

    public void findListGroupsByUserId(){
        apiService = RetrofitClient.getAPIService();
        Call<List<Group>> call = apiService.getGroupsByUserId(user.getId());
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if(response.isSuccessful()) {
                    root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    ls_groups = response.body();
                    //Log.d("Test",ls_groups.get(0).getName());
                    adapter = new MyGroupAdapter(root.getContext(), ls_groups);
                    rc_my_groups.setHasFixedSize(true);
                    rc_my_groups.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
                    rc_my_groups.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }

    public void findUserByUsername(){
        apiService = RetrofitClient.getAPIService();
        Call<User> call = apiService.findByUsername(user.getUsername());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    user = response.body();
                    findListGroupsByUserId();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }

    public void map(){
        rc_my_groups = root.findViewById(R.id.rv_my_groups);
    }
}