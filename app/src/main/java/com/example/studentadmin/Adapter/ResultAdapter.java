package com.example.studentadmin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.studentadmin.Model.ResultModel;
import com.example.studentadmin.R;


import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    Context context;
    List<ResultModel> list;

    public ResultAdapter(Context context, List<ResultModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item
                        , parent, false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name_student.setText(list.get(position).getName());
        holder.score_student.setText("Score :-"+Integer.toString(list.get(position).getScore()));
        holder.total_student.setText("Total :-"+Integer.toString(list.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 :list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_student, score_student, total_student;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_student = itemView.findViewById(R.id.name_user);
            score_student = itemView.findViewById(R.id.score);
            total_student = itemView.findViewById(R.id.total);
        }
    }
}
