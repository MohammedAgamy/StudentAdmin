package com.example.studentadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.studentadmin.Model.QuestionModel;
import com.example.studentadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class AddQuestionActivity extends AppCompatActivity {
    private EditText question;
    private RadioGroup options;

    private LinearLayout answer;
    private Button uploadBtn;
    private Dialog loadingDialog;
    private String lessonName;


    private int position;
    private QuestionModel questionModel;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AddQuestion");
        dialog();

        question = findViewById(R.id.question_edt);
        options = findViewById(R.id.options);
        answer = findViewById(R.id.answer);
        uploadBtn = findViewById(R.id.btn_upload);

        lessonName = getIntent().getStringExtra("LessonName");
        position = getIntent().getIntExtra("position", -1);
        if (position != -1) {
            questionModel = QuastionActivity.list.get(position);
            setData();
        }

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (question.getText().toString().isEmpty()) {
                    question.setError("Required");
                    return;
                }
                upload();
            }
        });
    }

    private void setData() {
        question.setText(questionModel.getQuastion());
        ((EditText) answer.getChildAt(0)).setText(questionModel.getA());
        ((EditText) answer.getChildAt(1)).setText(questionModel.getB());
        ((EditText) answer.getChildAt(2)).setText(questionModel.getC());
        ((EditText) answer.getChildAt(3)).setText(questionModel.getD());

        for (int i = 0; i < answer.getChildCount(); i++) {
            if (((EditText) answer.getChildAt(i)).getText().toString().equals(questionModel.getAnswer())) {
                RadioButton radioButton = (RadioButton) options.getChildAt(i);

                radioButton.setChecked(true);
                break;
            }

        }
    }

    private void upload() {
        int correct = -1;
        for (int i = 0; i < options.getChildCount(); i++) {

            EditText answers = (EditText) answer.getChildAt(i);
            if (answers.getText().toString().isEmpty()) {
                answers.setError("Required");
                return;
            }


            RadioButton radioButton = (RadioButton) options.getChildAt(i);
            if ((radioButton.isChecked())) {
                correct = i;
                break;
            }
        }
        if (correct == -1) {
            Toast.makeText(this, "Please mark the correct option", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.dismiss();
        HashMap<String, Object> map = new HashMap<>();
        map.put("correctANS", ((EditText) answer.getChildAt(correct)).getText().toString());
        map.put("optionA", ((EditText) answer.getChildAt(0)).getText().toString());
        map.put("optionB", ((EditText) answer.getChildAt(1)).getText().toString());
        map.put("optionC", ((EditText) answer.getChildAt(2)).getText().toString());
        map.put("optionD", ((EditText) answer.getChildAt(3)).getText().toString());
        map.put("question", question.getText().toString());

        loadingDialog.show();

        if (position != -1) {
            id = questionModel.getId();
        } else {
            id = UUID.randomUUID().toString();
        }

        FirebaseDatabase.getInstance().getReference()
                .child("SETS").child(lessonName)
                .child("questions").child(id)
                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    QuestionModel questionModel = new QuestionModel(id, map.get("question").toString()
                            , map.get("optionA").toString()
                            , map.get("optionB").toString()
                            , map.get("optionC").toString()
                            , map.get("optionD").toString()
                            , map.get("correctANS").toString()
                    );

                    if (position != -1) {
                        QuastionActivity.list.set(position, questionModel);

                    } else {
                        QuastionActivity.list.add(questionModel);
                    }
                    finish();
                } else {
                    Toast.makeText(AddQuestionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}