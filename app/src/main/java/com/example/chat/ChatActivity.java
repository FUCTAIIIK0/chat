package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    //layout
    private Toolbar mChatToolbar;
    private ImageView chatImage;
    private TextView name,online;
    private ImageButton chatAddBtn, chatSendBtn;
    private EditText chatMessage;

    //Database
    private String mChatUser;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAth;
    private String mCurrentUserID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Layout
        mChatToolbar = findViewById(R.id.chat_appBar);
        setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");

        chatImage = findViewById(R.id.chat_userImage);
        name =findViewById(R.id.chat_displayName);
        online = findViewById(R.id.chat_onlineStatus);
        chatAddBtn = findViewById(R.id.chat_addBtn);
        chatSendBtn = findViewById(R.id.chatSendBtn);
        chatMessage = findViewById(R.id.chat_editText);


        //Databse
        mChatUser = getIntent().getStringExtra("user_id");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAth = FirebaseAuth.getInstance();
        mCurrentUserID = mAth.getCurrentUser().getUid();

        //getUserData
        mRootRef.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //set Online
                String chat_online = dataSnapshot.child("online").getValue().toString();
                online.setText(chat_online);

                //set Name
                String chat_user_name = dataSnapshot.child("name").getValue().toString();
                name.setText(chat_user_name);
                //set Image
                chatImage = findViewById(R.id.chat_userImage);
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                Picasso.with(ChatActivity.this).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.userpic).into(chatImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        Picasso.with(ChatActivity.this).load(thumb_image).placeholder(R.drawable.userpic).into(chatImage);

                    }
                });
                //Click on image
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

        //Messaging
        mRootRef.child("Chat").child(mCurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(mChatUser)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/"+mCurrentUserID+"/"+mChatUser, chatAddMap);
                    chatUserMap.put("Chat/"+mChatUser+"/"+mCurrentUserID,chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError !=null){
                                Log.d("CHAT_LOG", databaseError.getMessage().toString());
                            }

                        }
                    });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });


    }

    private void sendMessage() {
        String message = chatMessage.getText().toString();


        if (!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/"+mCurrentUserID+"/"+mChatUser;
            String chat_user_ref = "messages/"+mChatUser+"/"+mCurrentUserID;

            DatabaseReference user_message_push = mRootRef.child("messages").
                    child(mCurrentUserID).child(mChatUser).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id, messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id, messageMap);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError !=null){
                        Log.d("CHAT_LOG", databaseError.getMessage().toString());
                    }
                }
            });

        }

    }
}
