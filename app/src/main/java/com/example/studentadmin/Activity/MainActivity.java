package com.example.studentadmin.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.studentadmin.fragment.PostsFragment;
import com.example.studentadmin.fragment.PostsStudentFragment;
import com.example.studentadmin.fragment.QuaizFragment;
import com.example.studentadmin.R;
import com.example.studentadmin.fragment.ResultFragment;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // id
        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // NavigationBar
        chipNavigationBar.setItemSelected(R.id.nav_post, true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new PostsFragment()).commit();
        bottomMenu();

    }

    @SuppressLint("NonConstantResourceId")
    private void bottomMenu() {

         getString(R.string.app_name);
        chipNavigationBar.setOnItemSelectedListener(i -> {
            Fragment fragment = null;
            switch (i) {
                case R.id.nav_post:
                    fragment = new PostsFragment();

                    break;
                case R.id.nav_post_student:
                    fragment = new PostsStudentFragment();

                    break;
                case R.id.nav_quaiz:
                    fragment = new QuaizFragment();
                    break;
                case R.id.nav_result:
                    fragment = new ResultFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment
                    , fragment).commit();
        });
    }



}