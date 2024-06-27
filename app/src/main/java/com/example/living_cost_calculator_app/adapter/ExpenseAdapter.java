package com.example.living_cost_calculator_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.living_cost_calculator_app.R;
import com.example.living_cost_calculator_app.models.Expense;
import com.example.living_cost_calculator_app.models.User;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder>{
    Context context;
    List<Expense> array;

    public ExpenseAdapter(Context context, List<Expense> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, null);
        ExpenseAdapter.MyViewHolder myViewHolder = new ExpenseAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Expense exp = array.get(position);
        holder.tvExpenseName.setText(exp.getName());
        //holder.tvShareWith.setText(String.join(", "));
        holder.tvCost.setText(exp.getCost() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return array == null ? 0 :array.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvExpenseName;
        public TextView tvShareWith;
        public TextView tvCost;
        public ImageButton btn_remove;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvShareWith = (TextView) itemView.findViewById(R.id.tvShareWith);
            tvExpenseName = (TextView) itemView.findViewById(R.id.tvExpenseName);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            btn_remove = (ImageButton) itemView.findViewById(R.id.btn_remove_expense);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
