package com.example.living_cost_calculator_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.models.Group;
import com.example.living_cost_calculator_app.models.User;

import java.util.List;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyViewHolder>{
    Context context;
    List<Group> array;

    public MyGroupAdapter(Context context, List<Group> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group, null);
        MyGroupAdapter.MyViewHolder myViewHolder = new MyGroupAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Group group = array.get(position);
        holder.tvGroupname.setText(group.getName());
        holder.tvNumMember.setText(group.getUsers().size()+" members");
    }

    @Override
    public int getItemCount() {
        return array == null ? 0 :array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvGroupname;
        public TextView tvNumMember;
        public Button btnViewDetail;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvGroupname = (TextView) itemView.findViewById(R.id.tvNameGroup);
            tvNumMember = (TextView) itemView.findViewById(R.id.tvNumMember);
            btnViewDetail = (Button) itemView.findViewById(R.id.btnViewGroupDetail);
            btnViewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
