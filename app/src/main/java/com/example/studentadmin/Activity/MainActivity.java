package com.example.studentadmin.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.studentadmin.fragment.PostsFragment;
import com.example.studentadmin.fragment.QuaizFragment;
import com.example.studentadmin.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // id
        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        //
//kkk

        // NavigationBar
        chipNavigationBar.setItemSelected(R.id.nav_post, true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new PostsFragment()).commit();
        bottomMenu();

    }

    @SuppressLint("NonConstantResourceId")
    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(i -> {
            Fragment fragment = null;
            switch (i) {
                case R.id.nav_post:
                    fragment = new PostsFragment();
                    break;
                case R.id.nav_quaiz:
                    fragment = new QuaizFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment
                    , fragment).commit();
        });
    }
}