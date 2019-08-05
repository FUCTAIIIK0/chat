package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        users_list =findViewById(R.id.users_list);
        users_list.setHasFixedSize(true);
        users_list.setLayoutManager(new LinearLayoutManager(this));
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
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users, int i) {
                usersViewHolder.setName(users.getName());
                usersViewHolder.setStatus(users.getStatus());
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

    }
}



