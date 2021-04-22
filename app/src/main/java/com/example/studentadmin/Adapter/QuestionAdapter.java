package com.example.studentadmin.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentadmin.Activity.AddQuestionActivity;
import com.example.studentadmin.Activity.QuastionActivity;
import com.example.studentadmin.Model.QuestionModel;
import com.example.studentadmin.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private List<QuestionModel> list;
    private String lesson;
    private DeleteListener listener;

    public QuestionAdapter(List<QuestionModel> list, String lesson, DeleteListener listener) {
        this.lesson = lesson;
        this.list = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quaistion_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        String question = list.get(position).getQuastion();
        String answer = list.get(position).getAnswer();
        holder.setData(question, answer, position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quastion, answer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quastion = itemView.findViewById(R.id.quaistion);
            answer = itemView.findViewById(R.id.answer);
        }

        private void setData(String question, String answer, int position) {
            this.quastion.setText(position + 1 + "." + question);
            this.answer.setText("Ans ." + answer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent editIntent = new Intent(itemView.getContext(), AddQuestionActivity.class);

                    editIntent.putExtra("LessonName", lesson);
                    editIntent.putExtra("position", position);
                    itemView.getContext().startActivity(editIntent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(position, list.get(position).getId());
                    return false;
                }
            });
        }
    }

    public interface DeleteListener {
        void onLongClick(int position, String id);
    }
}

