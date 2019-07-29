package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SettingsActivity extends AppCompatActivity {
    private Button imageBtn,statusBtn;
    private TextView displayName,status;
    //Database
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Layout
        imageBtn = findViewById(R.id.settings_imageBtn);
        status = findViewById(R.id.settings_statusBtn);
        displayName = findViewById(R.id.settings_displayName);
        status = findViewById(R.id.settings_statusText);
        //layout
        //Database
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Database

    }
}
