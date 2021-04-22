package com.example.studentadmin.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentadmin.Activity.MainActivity;
import com.example.studentadmin.Adapter.PostAdapter;
import com.example.studentadmin.Model.PostModel;
import com.example.studentadmin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PostsFragment extends Fragment implements View.OnClickListener , PostAdapter.OnClick {
    //filed find view ....
    private EditText mEPost;
    private Button mBtnPost;
    private RecyclerView mViewRecycler;
    //firebase
    FirebaseFirestore fireStore;

    //Mode
    PostModel postModel;

    //Adapter
    PostAdapter mAdapterPost;

    //filed dataType
    long Post_id ;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        iniView(view);

        RetriveData() ;
    }

    //Start fined all filed
    private void iniView(View view) {
        //filed layout
        mEPost = view.findViewById(R.id.edt_post);
        mBtnPost = view.findViewById(R.id.btn_post);
        mBtnPost.setOnClickListener(this);
        mViewRecycler = view.findViewById(R.id.recycler_post);

        //firebase
        fireStore = FirebaseFirestore.getInstance();

    }//end fined all filed


    //Start On Click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                UploadPost();
                break;

        }
    }//Start On Click


    //Start UploadPost
    private void UploadPost() {
        String Post = mEPost.getText().toString();

        if (Post.isEmpty()) {
            ShowDialog();
        }

        else
        {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            Post_id = System.currentTimeMillis();
            postModel = new PostModel();
            postModel.setPost_id(Post_id);
            postModel.setName(null);
            postModel.setPost(Post);
            postModel.setImage(null);
            postModel.setTime(date);
            fireStore.collection("AdminPosts").document(String.valueOf(Post_id)).set(postModel)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(getActivity(), "Post Create....", Toast.LENGTH_SHORT).show();
                        mEPost.setText(null);

                    });
        }
    }//end UploadPost


    ///start dialog if user dont enter data
    private void ShowDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.item_dialog);
        Button dialogButton = dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();

    }///end dialog if user dont enter data


    ///Start retrive data from fire base in recycler view
    public void RetriveData() {
        CollectionReference collection = fireStore.collection("AdminPosts");
        Query query = collection.orderBy("time", Query.Direction.ASCENDING);
        collection.orderBy("time");
        FirestoreRecyclerOptions<PostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel.class)
                .build();

        // Adapter ............................
        mAdapterPost = new PostAdapter(recyclerOptions ,this::OnIttemClick);
        mViewRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterPost.notifyDataSetChanged();
        mViewRecycler.setAdapter(mAdapterPost);

    } ///end retrive data from fire base in recycler view

    @Override
    public void onStart() {
        super.onStart();
        mAdapterPost.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapterPost.stopListening();
    }

    @Override
    public void OnIttemClick(long click) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundel = new Bundle();
        bundel.putLong("post_id", click);
        fragment.setArguments(bundel);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.cont, fragment);
        ft.commit();
    }
}