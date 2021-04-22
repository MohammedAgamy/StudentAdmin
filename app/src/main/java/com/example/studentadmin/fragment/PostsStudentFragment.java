package com.example.studentadmin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentadmin.Activity.MainActivity;
import com.example.studentadmin.Adapter.PostStudentAdapter;
import com.example.studentadmin.Model.PostModel;
import com.example.studentadmin.Model.PostStudentModel;
import com.example.studentadmin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PostsStudentFragment extends Fragment implements PostStudentAdapter.OnClick_student {
    PostStudentModel mPostModel;
    PostStudentAdapter mAdapterStudent;
    FirebaseFirestore mFireStore;
    RecyclerView mRecyclerStudent;



    public PostsStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        iniview(view);
    }

    private void iniview(View view) {
        mRecyclerStudent = view.findViewById(R.id.list_post_sTUDENT);
        mFireStore = FirebaseFirestore.getInstance();
        retriveDataStudent();
    }

    //start retrive data from fire base in recycler view
    private void retriveDataStudent() {
        CollectionReference collection = mFireStore.collection("Posts");
        Query query = collection.orderBy("time", Query.Direction.ASCENDING);
        collection.orderBy("time");
        FirestoreRecyclerOptions<PostStudentModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<PostStudentModel>()
                .setQuery(query, PostStudentModel.class)
                .build();

        // Adapter ............................
        mAdapterStudent = new PostStudentAdapter(recyclerOptions, this);
        mRecyclerStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterStudent.notifyDataSetChanged();
        mRecyclerStudent.setAdapter(mAdapterStudent);

    } ///end retrive data from fire base in recycler view

    @Override
    public void onStart() {
        super.onStart();
        mAdapterStudent.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapterStudent.stopListening();
    }

    @Override
    public void OnIttemClick(long click) {
        Log.d("TAG", String.valueOf(click));
        CommentStudentFragment fragment = new CommentStudentFragment();
        Bundle bundel = new Bundle();
        bundel.putLong("comment_post_id", click);
        fragment.setArguments(bundel);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.cont, fragment);
        ft.commit();
    }
}