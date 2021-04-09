package com.example.studentadmin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.studentadmin.Model.CommentModel;
import com.example.studentadmin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends FirestoreRecyclerAdapter<CommentModel, CommentAdapter.item_Comment> {





    public CommentAdapter(FirestoreRecyclerOptions<CommentModel> recyclerOptions) {
        super(recyclerOptions);
    }


    @Override
    protected void onBindViewHolder(@NonNull item_Comment holder, int position, @NonNull CommentModel model) {

        holder.mImage.setImageURI(model.getImage());
        holder.mName.setText(model.getName());
        holder.mTime.setText(model.getTime());
        holder.mPost.setText(model.getComment());


    }

    @NonNull
    @Override
    public item_Comment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentAdapter.item_Comment item_post = new CommentAdapter.item_Comment(view);
        return item_post;
    }

    class item_Comment extends RecyclerView.ViewHolder {
        CircleImageView mImage;
        TextView mName, mTime, mPost ;


        public item_Comment(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.image_profilec);
            mName = itemView.findViewById(R.id.name_userc);
            mTime = itemView.findViewById(R.id.timec);
            mPost = itemView.findViewById(R.id.postc);

        }
    }

    public interface OnClick {
        void OnIttemClick(long click);
    }
}
