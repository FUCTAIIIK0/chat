package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {
    //layout
    private Toolbar mChatToolbar;
    private ImageView chatImage;

    //Database
    private String mChatUser;
    private DatabaseReference mRootRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Databse
        mChatUser = getIntent().getStringExtra("user_id");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("Users").child(mChatUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String chat_user_name = dataSnapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle(chat_user_name);

                chatImage = findViewById(R.id.chat_userImage);
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                Log.d("thumb_image", "onDataChange: "+thumb_image);

                Picasso.with(ChatActivity.this).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.userpic).into(chatImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Picasso.with(ChatActivity.this).load(thumb_image).placeholder(R.drawable.userpic).into(chatImage);

                    }
                });
                chatImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profiliIntent = new Intent(ChatActivity.this,ProfileActivity.class);
                        profiliIntent.putExtra("user_id",mChatUser);
                        startActivity(profiliIntent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Layout
        mChatToolbar = findViewById(R.id.chat_appBar);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chatImage = findViewById(R.id.chat_userImage);






    }
}
