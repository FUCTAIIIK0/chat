package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


public class UsersActivity extends AppCompatActivity {
    private Toolbar users_toolBar;
    private RecyclerView users_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        //Layout
        users_toolBar = findViewById(R.id.usersAppBar);
        setSupportActionBar(users_toolBar);
        getSupportActionBar().setTitle("Users Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        users_list =findViewById(R.id.users_list);

    }
}
