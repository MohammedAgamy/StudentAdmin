package com.example.studentadmin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentadmin.Adapter.CommentAdapter;
import com.example.studentadmin.Model.CommentModel;
import com.example.studentadmin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentStudentFragment extends Fragment implements View.OnClickListener {

    private String id;
    CommentAdapter mCommentAdapter;
    CommentModel mCommentModel;
    FirebaseFirestore mFireStore;
    RecyclerView mRecyclerComment;
    Button mBtnComment;
    EditText mEComment;

    public CommentStudentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = String.valueOf(bundle.getLong("comment_post_id"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniview(view);
        RetriveData();
    }

    private void iniview(View view) {
        mEComment = view.findViewById(R.id.edt_comment_d);
        mBtnComment = view.findViewById(R.id.btn_comment_d);
        mBtnComment.setOnClickListener(this);
        mRecyclerComment = view.findViewById(R.id.list_comment_d);
        mFireStore = FirebaseFirestore.getInstance();
    }

    public void RetriveData() {
        CollectionReference collection = mFireStore.collection("AdminPosts")
                .document(id)
                .collection("Comments");
        Query query = collection.orderBy("time", Query.Direction.ASCENDING);
        collection.orderBy("time");
        FirestoreRecyclerOptions<CommentModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<CommentModel>()
                .setQuery(query, CommentModel.class)
                .build();

        // Adapter ............................
        mCommentAdapter = new CommentAdapter(recyclerOptions);
        mRecyclerComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentAdapter.notifyDataSetChanged();
        mRecyclerComment.setAdapter(mCommentAdapter);

    } ///end retrive data from fire base in recycler view

    @Override
    public void onStart() {
        super.onStart();
        mCommentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCommentAdapter.stopListening();
    }

    private void UploudComent() {
        String comment = mEComment.getText().toString();

        if (comment.isEmpty()) {
            Toast.makeText(getActivity(), "Add Comment", Toast.LENGTH_SHORT).show();
        } else {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            long Comment_id = System.currentTimeMillis();
            mCommentModel = new CommentModel();
            mCommentModel.setName(null);
            mCommentModel.setImage(null);
            mCommentModel.setTime(date);
            mCommentModel.setComment(comment);
            mCommentModel.setComment_id(Comment_id);

            mFireStore.collection("AdminPosts").document(id).collection("Comments")
                    .document(String.valueOf(Comment_id))
                    .set(mCommentModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Comment Created", Toast.LENGTH_SHORT).show();
                            mEComment.setText(null);
                        }
                    });


        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_comment_d:
                UploudComent();
                break;
        }
    }
}