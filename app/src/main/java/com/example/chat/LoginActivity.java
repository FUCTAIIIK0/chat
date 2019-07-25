package com.example.chat;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Toolbar loginToolbar;
    private TextInputLayout loginEmail, loginPassword;
    private Button loginAccountBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Layout
        loginToolbar = findViewById(R.id.loginToolBar);
        setSupportActionBar(loginToolbar);
        getSupportActionBar().setTitle("Login page");


        loginEmail = findViewById(R.id.loginEmailEdittext);
        loginPassword = findViewById(R.id.loginPassword);

        loginAccountBtn = findViewById(R.id.createBtn);
        loginAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

}
