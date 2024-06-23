package com.example.living_cost_calculator_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.models.User;
import com.example.living_cost_calculator_app.utils.APIService;
import com.example.living_cost_calculator_app.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    Context context;
    List<User> array;
    EditText txt_username;

    public UserAdapter(Context context, List<User> array, EditText txt_username) {
        this.context = context;
        this.array = array;
        this.txt_username = txt_username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, null);
        MyViewHolder myViewHolder = new MyViewHolder(view, txt_username);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = array.get(position);
        holder.tvEmail.setText(user.getEmail());
        holder.tvUsername.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return array == null ? 0 :array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvUsername;
        public TextView tvEmail;
        public MyViewHolder(@NonNull View itemView, EditText txt_username){
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txt_username != null) {
                        txt_username.setText(tvUsername.getText());
                    }
                }
            });
        }

    }
}
