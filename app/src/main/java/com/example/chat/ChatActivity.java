package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private RecyclerView mMessagesList;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;
    private DatabaseReference mMessageDatabase;

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
                String lastSeenTime;

                if (!chat_online.equals("online")){
                    //Convert to last seen view
                    Integer timeInteget = Integer.parseInt(chat_online);
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    lastSeenTime = getTimeAgo.getTimeAgo(timeInteget,getApplicationContext());
                }else {
                    lastSeenTime = "online";
                }

                online.setText(lastSeenTime);

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
                chatMessage.setText(" ");

            }
        });

  /*      chatSendBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("TUCH", "onTouch: ");
            }
        });*/

       // Log.d("TUCH", "onTouch: ");


        mAdapter = new MessageAdapter(messagesList);

        mMessagesList = findViewById(R.id.chat_messages_list);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        loadMessages();
    }

    private void loadMessages() {

        mRootRef.child("messages").child(mCurrentUserID).child(mChatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Messages message= dataSnapshot.getValue(Messages.class);
                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                mMessagesList.smoothScrollToPosition(mMessagesList.getAdapter().getItemCount());


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage() {

    mMessagesList.smoothScrollToPosition(mMessagesList.getAdapter().getItemCount());

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
            messageMap.put("from",mCurrentUserID);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id, messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id, messageMap);

            chatMessage.setText("");

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
