package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {
    Button regDisplayName,regEmail,regPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regDisplayName = findViewById(R.id.regDisplayName);
        regEmail = findViewById(R.id.regEmailEdittext);
        regPassword = findViewById(R.id.regPassword);

    }
}
