package com.example.studentadmin.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.studentadmin.Activity.MainActivity;
import com.example.studentadmin.Activity.QuastionActivity;
import com.example.studentadmin.Activity.ResultActivity;
import com.example.studentadmin.Adapter.LessonAdapter;
import com.example.studentadmin.Model.LessonModel;
import com.example.studentadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ResultFragment extends Fragment {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private RecyclerView recyclerView;
    private Dialog loadingDialog, categoryDialog;

    private LessonAdapter adapter;
    private List<LessonModel> list;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.round_cornner));
        loadingDialog.setCancelable(false);


        recyclerView = view.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new LessonAdapter(list, this::onItemLessonClicked, new LessonAdapter.DeleteListener() {
            @Override
            public void onDelete(String Key, int position) {
                new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog)
                        .setTitle("Delete Category")
                        .setMessage("Are you sure,you want to delete this category?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef.child("Lesson")
                                        .child(Key)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    myRef.child("score").child(list.get(position).getNameLesson()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                list.remove(position);
                                                                adapter.notifyDataSetChanged();
                                                            } else {
                                                                Toast.makeText(getContext(), "failed to delete", Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });


                                                } else {
                                                    Toast.makeText(getContext(), "failed to delete", Toast.LENGTH_SHORT).show();
                                                    loadingDialog.dismiss();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        recyclerView.setAdapter(adapter);

        loadingDialog.show();

        myRef.child("Lesson")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            list.add(new LessonModel(snapshot1.child("nameLesson")
                                    .getValue().toString(),
                                    snapshot1.getKey()
                            ));
                        }
                        adapter.notifyDataSetChanged();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        getActivity().finish();
                    }
                });
    }

    public void onItemLessonClicked(LessonModel lessonModel, int position) {
        Intent setIntent = new Intent(getContext(), ResultActivity.class);
        setIntent.putExtra("title", lessonModel.getNameLesson());
        setIntent.putExtra("Key", lessonModel.getKey());
        setIntent.putExtra("position", position);
        getActivity().startActivity(setIntent);
    }
}