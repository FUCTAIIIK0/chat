package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    //Layout
    private ImageView profileImage;
    private TextView profileDisplayName,profileStatus,profileTotalFriends;
    private Button profileSendReqBtn;
    //Database
    private DatabaseReference mUsersDatabase;
    //ProggressDialog
    private ProgressDialog mProgressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Layout
        profileDisplayName = findViewById(R.id.profile_displayName);
        profileStatus = findViewById(R.id.profile_status);
        profileImage = findViewById(R.id.profile_image);
        profileTotalFriends = findViewById(R.id.profile_totalFriends);
        profileSendReqBtn = findViewById(R.id.profile_send_req_btn);

        //ProgressDialog
        mProgressdialog = new ProgressDialog(this);
        mProgressdialog.setTitle("Loading User Data");
        mProgressdialog.setMessage("Please wait while we load user data.");
        mProgressdialog.setCanceledOnTouchOutside(false);
        mProgressdialog.show();


        //Database
        String user_id = getIntent().getStringExtra("user_id");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                profileDisplayName.setText(display_name);
                profileStatus.setText(status);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.userpic).into(profileImage);
                mProgressdialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Temp


    }
}
