package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    //Layout
    private CircleImageView mImage;
    private Button imageBtn,statusBtn;
    private TextView mName, mStatus;
    //Database
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Layout
        imageBtn = findViewById(R.id.settings_imageBtn);
        statusBtn = findViewById(R.id.settings_statusBtn);
        mName = findViewById(R.id.settings_displayName);
        mStatus = findViewById(R.id.settings_statusText);
        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status_value = mStatus.getText().toString();
                Intent statusIntent = new Intent(SettingsActivity.this,StatusActivity.class);
                statusIntent.putExtra("status_value",status_value);
                startActivity(statusIntent);
            }
        });

        //layout
        //Database
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUID = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = "null";
                String image = "null";
                String status = "null";
                String thumb_image = "null";
                if (dataSnapshot.child("name").getValue() != null){
                     name = dataSnapshot.child("name").getValue().toString();
                }
                if (dataSnapshot.child("image").getValue() != null){
                    image = dataSnapshot.child("image").getValue().toString();
                }
                if (dataSnapshot.child("status").getValue() != null){
                    status = dataSnapshot.child("status").getValue().toString();
                }
                if (dataSnapshot.child("thumb_image").getValue() != null){
                    thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                }

                mName.setText(name);
                mStatus.setText(status);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Database

    }
}
