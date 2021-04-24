package com.example.studentadmin.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.studentadmin.Adapter.QuestionAdapter;
import com.example.studentadmin.Model.QuestionModel;
import com.example.studentadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuastionActivity extends AppCompatActivity {
    private Button add, excel;
    RecyclerView recyclerView;

    private QuestionAdapter adapter;
    public static List<QuestionModel> list;
    private Dialog loadingDialog;
    private DatabaseReference myRef;
    private String lessonName;

    public static final int CELL_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quastion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        myRef = FirebaseDatabase.getInstance().getReference();
        dialog();

        add = findViewById(R.id.btn_add);
        excel = findViewById(R.id.btn_excel);
        recyclerView = findViewById(R.id.recycler_view);
        lessonName = getIntent().getStringExtra("title");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new QuestionAdapter(list,lessonName, new QuestionAdapter.DeleteListener() {
            @Override
            public void onLongClick(final int position, final String id) {

                new AlertDialog.Builder(QuastionActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                        .setTitle("Delete Question")
                        .setMessage("Are you sure, you want to delete this question?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadingDialog.show();
                                myRef.child("SETS").child(lessonName)
                                        .child("question")
                                        .child(id).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            list.remove(position);
                                            adapter.notifyItemRemoved(position);
                                        }else {

                                            Toast.makeText(QuastionActivity.this, "Failed To Delete", Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        recyclerView.setAdapter(adapter);

        getData(lessonName);

        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(QuastionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addQuestion = new Intent(QuastionActivity.this, AddQuestionActivity.class);
                addQuestion.putExtra("LessonName", lessonName);
                startActivity(addQuestion);
            }
        });

    }  //  on create


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFile();
            } else {
                Toast.makeText(this, "please Grant permissions!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "SelectFile"), 102);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                String filepatch = data.getData().getPath();
                if (filepatch.endsWith(".xlsx")) {
                    Toast.makeText(this, "file selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "please choose an Excel file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getData(String lessonName) {
        loadingDialog.show();
        myRef.child("SETS").child(lessonName)
                .child("questions")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //  بحط الاسئله في الفاير بيس

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String id = snapshot1.getKey();
                            String question = snapshot1.child("question").getValue().toString();
                            String a = snapshot1.child("optionA").getValue().toString();
                            String b = snapshot1.child("optionB").getValue().toString();
                            String c = snapshot1.child("optionC").getValue().toString();
                            String d = snapshot1.child("optionD").getValue().toString();
                            String correctANS = snapshot1.child("correctANS").getValue().toString();
                            list.add(new QuestionModel(id, question, a, b, c, d, correctANS));
                        }
                        loadingDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuastionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        finish();
                    }
                });
    }
    private void dialog() {
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_cornner));
        loadingDialog.setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}