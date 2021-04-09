package com.example.studentadmin.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentadmin.Model.PostModel;
import com.example.studentadmin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends FirestoreRecyclerAdapter<PostModel, PostAdapter.item_post> {
    private static final String TAG = "AppDepug";
    OnClick onClick;
    OnClick_Like onClick_like ;
    public String id;
    int count = 0;


  // public PostAdapter(@NonNull FirestoreRecyclerOptions<PostModel> options) {
   //     super(options);
  //  }

    public PostAdapter(@NonNull FirestoreRecyclerOptions<PostModel> options ,OnClick onClick) {
        super(options);
        this.onClick=onClick ;
    }

    @Override
    protected void onBindViewHolder(@NonNull item_post holder, int position, @NonNull PostModel model) {
        // final PostModel post = holder.get(position);
        id=String.valueOf(model.getPost_id());

        holder.mImage.setImageURI(model.getImage());
        holder.mName.setText(model.getName());
        holder.mTime.setText(model.getTime());
        holder.mPost.setText(model.getPost());
        holder.mCounter.setText(model.getCounter());



        /**
         * btn comment
         */
        holder.btn_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, String.valueOf(model.getPost_id()));
                onClick.OnIttemClick(model.getPost_id());
            }


        });

        /**
         * btn like
         */
        holder.btn_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, String.valueOf(model.getLike()));
                holder.btn_Like_red.setVisibility(View.VISIBLE);
                count ++ ;
                onClick_like.OnIttemClick_Like(model.getPost_id() ,count);
                holder.mCounter.setText(String.valueOf(count));

            }
        });
    }

    @NonNull
    @Override
    public item_post onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        item_post item_post = new item_post(view);
        return item_post;
    }


    class item_post extends RecyclerView.ViewHolder {
        CircleImageView mImage;
        TextView mName, mTime, mPost, mCounter;
        ImageView btn_Comment ,btn_Like ,btn_Like_red;

        public item_post(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.image_profile);
            btn_Like=itemView.findViewById(R.id.Like);
            btn_Like_red=itemView.findViewById(R.id.Like_red);
            mName = itemView.findViewById(R.id.name_user);
            mTime = itemView.findViewById(R.id.time);
            mPost = itemView.findViewById(R.id.post);
            mCounter = itemView.findViewById(R.id.Counter);
            btn_Comment = itemView.findViewById(R.id.comment);

        }
    }

    public interface OnClick {
        void OnIttemClick(long click);
    }


    public interface OnClick_Like {
        void OnIttemClick_Like(long click_Like, int count);
    }
}
