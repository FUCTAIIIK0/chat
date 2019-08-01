package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;


public class UsersActivity extends AppCompatActivity {
    private Toolbar users_toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        users_toolBar = findViewById(R.id.usersToolBar);
        setSupportActionBar(users_toolBar);
        getSupportActionBar().setTitle("Users Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
