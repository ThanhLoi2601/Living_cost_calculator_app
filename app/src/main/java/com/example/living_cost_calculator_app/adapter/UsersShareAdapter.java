package com.example.living_cost_calculator_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.models.User;

import java.util.List;

public class UsersShareAdapter extends RecyclerView.Adapter<UsersShareAdapter.MyViewHolder>{
    Context context;
    List<User> array;

    public UsersShareAdapter(Context context, List<User> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_users_share, null);
        UsersShareAdapter.MyViewHolder myViewHolder = new UsersShareAdapter.MyViewHolder(view);
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
        public TextView tvCost;
        public ImageButton btn_remove;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            btn_remove = (ImageButton) itemView.findViewById(R.id.btn_remove_user_share);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
