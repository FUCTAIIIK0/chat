
package com.example.chat;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);
        return new MessageViewHolder(v);
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView messageTime;
        public CircleImageView profileImage;
        public TextView displayName;


        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_image);
            displayName = (TextView) view.findViewById(R.id.message_display_name);
            messageTime = view.findViewById(R.id.message_time);
            messageText = view.findViewById(R.id.message_text_layout);

        }
    }
    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        Messages c = mMessageList.get(i);
        String from_user = c.getFrom();
        String message_type = c.getType();
        String userid = c.getMessage();

        long time = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  dd MMM");
        Date resultdate = new Date(time);
        String date = sdf.format(time);
        //String date = sdf.toString();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(c.getFrom());
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();
                viewHolder.displayName.setText(name);
                Picasso.with(viewHolder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.userpic).into(viewHolder.profileImage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        if(message_type.equals("text")) {
            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageTime.setText(date);
            //viewHolder.profileImage.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.userpic).into(viewHolder.profileImage);
        }
    }
            @Override
            public int getItemCount() {
            return mMessageList.size();
            }
}


