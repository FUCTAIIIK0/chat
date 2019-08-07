package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersActivity extends AppCompatActivity {
    private Toolbar users_toolBar;
    private RecyclerView users_list;
    private DatabaseReference mUsersDatabase;

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
        users_list.setHasFixedSize(true);
        users_list.setLayoutManager(new LinearLayoutManager(this));
        //Database
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users, int position) {
                usersViewHolder.setName(users.getName());
                usersViewHolder.setStatus(users.getStatus());
                usersViewHolder.setOnlineStatus(users.getOnlineStatus());
                usersViewHolder.setUsersImage(users.getThumb_image(), getApplicationContext());

                String user_id = getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

/*                        Intent profiliIntent = new Intent(UsersActivity.this,ProfileActivity.class);
                        profiliIntent.putExtra("user_id",user_id);
                        startActivity(profiliIntent);*/
                        CharSequence options[] = new CharSequence[]{"Open Profile","Send message"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                        builder.setTitle("Select Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i ==0){
                                    Intent profiliIntent = new Intent(UsersActivity.this,ProfileActivity.class);
                                    profiliIntent.putExtra("user_id",user_id);
                                    startActivity(profiliIntent);
                                }
                                if (i==1){
                                    Intent chatIntent = new Intent(UsersActivity.this,ChatActivity.class);
                                    chatIntent.putExtra("user_id",user_id);
                                    startActivity(chatIntent);                                }

                            }
                        });
                        builder.show();
                    }
                });

            }
        };
        users_list.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView userNameViev = mView.findViewById(R.id.users_singleName);
            userNameViev.setText(name);
        }
        public void setStatus(String status){
            TextView userStatusViev = mView.findViewById(R.id.users_singleStatus);
            userStatusViev.setText(status);
        }
        public void setOnlineStatus(String onlineStatus){
            TextView userOnlineStatus = mView.findViewById(R.id.users_onlineStatus);
            userOnlineStatus.setText(onlineStatus);
        }

        public void setUsersImage(String thumb_image, Context ctx){
            CircleImageView userImageView = mView.findViewById(R.id.users_singleImage);
            Picasso.with(ctx).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.userpic).into(userImageView, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                    Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.userpic).into(userImageView);

                }
            });

        }

    }
}



