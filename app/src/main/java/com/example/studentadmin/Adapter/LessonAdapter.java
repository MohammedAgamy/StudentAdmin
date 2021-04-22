package com.example.studentadmin.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentadmin.Activity.QuastionActivity;
import com.example.studentadmin.Model.LessonModel;
import com.example.studentadmin.R;
import com.example.studentadmin.listener.ItemLessonListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.MyViewHolder> {
    private List<LessonModel> lessonModels;
    public DeleteListener deleteListener;

    public LessonAdapter( List<LessonModel> lessonModels,DeleteListener deleteListener) {
        this.deleteListener=deleteListener;
        this.lessonModels = lessonModels;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson
                        , parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.setDate(lessonModels.get(position).getImageLesson(), lessonModels.get(position).getNameLesson()
               ,lessonModels.get(position).getKey(),position);
    }

    @Override
    public int getItemCount() {
        return lessonModels == null ? 0 : lessonModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txt_nameCourse;
        private ImageButton delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            txt_nameCourse = itemView.findViewById(R.id.title);
            delete=itemView.findViewById(R.id.delete);
        }
        private void setDate(final String url, final String title,final String Key,final int position) {

            Picasso.get().load(url)
                    .placeholder(R.drawable.student)
                    .into(imageView);
            this.txt_nameCourse.setText(title);

            itemView.setOnClickListener(v -> {
                Intent setIntent = new Intent(itemView.getContext(), QuastionActivity.class);
                setIntent.putExtra("title", title);
                setIntent.putExtra("Key", Key);
                setIntent.putExtra("position", position);
                itemView.getContext().startActivity(setIntent);
            });
            delete.setOnClickListener(v ->
                    deleteListener.onDelete(Key,position));
        }
    }
    public interface DeleteListener{
        public void onDelete(String Key,int position);
    }
}
