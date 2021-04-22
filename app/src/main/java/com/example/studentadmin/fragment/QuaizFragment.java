package com.example.studentadmin.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentadmin.Activity.MainActivity;
import com.example.studentadmin.Adapter.LessonAdapter;
import com.example.studentadmin.Model.LessonModel;
import com.example.studentadmin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class QuaizFragment extends Fragment {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private Dialog loadingDialog, categoryDialog;
    private CircleImageView addImage;
    private EditText lessonname;
    private Button addBtn;
    private Uri image;
    private String downloadUrl;
    private LessonAdapter adapter;
    private List<LessonModel> list;


    public QuaizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quaiz, container, false);
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

        setLessonDialog();

        recyclerView = view.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new LessonAdapter(list, (Key, position) ->
                new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog)
                .setTitle("Delete Category")
                .setMessage("Are you sure,you want to delete this category?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.child("Lesson").child(Key).removeValue().addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {
                                myRef.child("SETS").child(list.get(position).getImageLesson()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        });
                    }
                })
                .setNegativeButton("cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
        recyclerView.setAdapter(adapter);

        loadingDialog.show();


        myRef.child("Lesson").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    list.add(snapshot1.getValue(LessonModel.class));
                    list.add(new LessonModel(snapshot1.child("nameLesson").getValue().toString(),
                            snapshot1.child("imageLesson").getValue().toString(),
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setLessonDialog() {
        categoryDialog = new Dialog(getContext());
        categoryDialog.setContentView(R.layout.add_lesson_dialog);
        categoryDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.rounded_box));
        categoryDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        categoryDialog.setCancelable(false);

        addImage = categoryDialog.findViewById(R.id.circle_image);
        lessonname = categoryDialog.findViewById(R.id.edit_category_name);
        addBtn = categoryDialog.findViewById(R.id.btn_add);

        addImage.setOnClickListener(view -> {
            Intent galleryintent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryintent, 101);
        });


        addBtn.setOnClickListener(view -> {
            if (lessonname.getText() == null || lessonname.getText().toString().isEmpty()) {
                lessonname.setError("name required");
                return;
            }
            for (LessonModel model : list) {
                if (lessonname.getText().toString().equals(model.getNameLesson())) {
                    lessonname.setError("Lesson name Already Present!");
                    return;
                }
            }

            if (image == null) {
                Toast.makeText(getContext(), "Please Select Your Image", Toast.LENGTH_SHORT).show();
            }


            categoryDialog.dismiss();


             uploadData();
           // uploadLessonName();
          //  uploadProfileImage();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 101) {
            if (requestCode == RESULT_OK) {
                image = data.getData();
                addImage.setImageURI(image);
            }
        }
    }
    private void uploadData() {
        loadingDialog.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageReference = storageReference.child("categories").child(image.getLastPathSegment());

        UploadTask uploadTask = imageReference.putFile(image);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return imageReference.child("categories").getDownloadUrl().addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    downloadUrl = task1.getResult().toString();
                    uploadLessonName();

                } else {
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {

                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadLessonName() {
        Map<String, Object> map = new HashMap<>();
        map.put("nameLesson", lessonname.getText().toString());
        map.put("imageLesson", downloadUrl);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Lesson").child("lesson" + (list.size() ))
                .setValue(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.add(new LessonModel(lessonname.getText().toString()
                                , downloadUrl
                                , "lesson" + (list.size())));
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.icon_add) {
            categoryDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}


/*

    private void uploadData() {
        loadingDialog.show();
         StorageReference storageReference = FirebaseStorage.getInstance().getReference();
         StorageReference imageReference = storageReference.child("categories").child(image.getLastPathSegment());

        UploadTask uploadTask = imageReference.putFile(image);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return imageReference.child("categories").getDownloadUrl().addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    downloadUrl = task1.getResult().toString();
                    uploadLessonName();

                } else {
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {

                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
 */
/*
 private void uploadProfileImage() {
        storageReference.child("ProfileImages").putFile(image)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                            getProfileUrl();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getProfileUrl() {
        storageReference.child("ProfileImages").child(auth.getUid()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadUrl = task.getResult().toString();

                            Picasso.get().load(downloadUrl).centerCrop()
                                    .into(addImage);
                            Map<String, Object> map = new HashMap<>();
                            map.put("profileUrl", downloadUrl);
                            uploadLessonName();

                            //  SharedPreferences sharedPreferences.edit().putString("userProfileLink",image).apply();

                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
 */